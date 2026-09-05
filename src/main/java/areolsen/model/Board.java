package areolsen.model;

import areolsen.model.grid.Grid;
import areolsen.model.grid.HashGrid;
import areolsen.model.grid.Position;
import areolsen.model.pieces.BishopPiece;
import areolsen.model.pieces.KingPiece;
import areolsen.model.pieces.KnightPiece;
import areolsen.model.pieces.PawnPiece;
import areolsen.model.pieces.QueenPiece;
import areolsen.model.pieces.RookPiece;
import areolsen.sound.SoundPlayer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Basic ChessBoard implementation that contains chess board logic. Such as changing sides, getting
 * pieces, promoting pawns, moving pieces, game over handling, reseting...
 */
public class Board implements Iterable<Piece>, Cloneable {
  private HashGrid<Piece> board = new HashGrid<>(8, 8);
  private Side side = Side.WHITE;

  /** Construct new chessboard with all pieces initialized. */
  public Board() {
    initAllPieces();
  }

  /** Construct a new chessboard with no pieces. */
  public Board emptyBoard() {
    Board emptyBoard = new Board();
    emptyBoard.board = new HashGrid<>(8, 8);
    return emptyBoard;
  }

  private void initAllPieces() {
    initPawns(Side.WHITE);
    initPawns(Side.BLACK);
    initMajors(Side.WHITE);
    initMajors(Side.BLACK);
  }

  private void initPawns(Side side) {
    int row = side == Side.WHITE ? 1 : 6;
    for (int i = 0; i < 8; i++) {
      new PawnPiece(this, new Position(i, row), side);
    }
  }

  private void initMajors(Side side) {
    int row = side == Side.WHITE ? 0 : 7;
    new RookPiece(this, new Position(0, row), side);
    new RookPiece(this, new Position(7, row), side);
    new KnightPiece(this, new Position(1, row), side);
    new KnightPiece(this, new Position(6, row), side);
    new BishopPiece(this, new Position(2, row), side);
    new BishopPiece(this, new Position(5, row), side);
    new QueenPiece(this, new Position(3, row), side);
    new KingPiece(this, new Position(4, row), side);
  }

  /** Reset board state to start state. */
  public void reset() {
    board = new HashGrid<>(8, 8);
    side = Side.WHITE;
    initAllPieces();
  }

  /**
   * Gets width (column count) of board.
   *
   * @return int of columns in board.
   */
  public int getWidth() {
    return board.getWidth();
  }

  /**
   * Gets height (row count) of board.
   *
   * @return int of rows in board.
   */
  public int getHeight() {
    return board.getHeight();
  }

  /**
   * Gets underlying grid datastructure of board.
   *
   * @return Grid datastructure pieces are stored on.
   */
  protected Grid<Piece> getGrid() {
    return this.board;
  }

  /** Flips the current side to other side. */
  public void changeSide() {
    if (side == Side.WHITE) {
      side = Side.BLACK;
      return;
    }
    side = Side.WHITE;
  }

  /**
   * Gets current chess side.
   *
   * @return ChessSide current playing chess side.
   */
  public Side getSide() {
    return this.side;
  }

  /**
   * Get legal moves as a list of legal end positions.
   *
   * @param start Position of piece to get legal moves from.
   * @return List of legal moves' end positions.
   */
  public List<Move> getLegalMoves(Position start) {
    Optional<Piece> pieceOptional = getPiece(start);
    if (pieceOptional.isEmpty()) {
      return new ArrayList<>();
    }
    return pieceOptional.get().getLegalMoves();
  }

  /**
   * Gets a list of all legal moves for a side.
   *
   * @param side side to get legal moves for
   * @return list of legal moves.
   */
  public List<Move> getAllLegalMoves(Side side) {
    List<Move> moves = new ArrayList<>();
    for (var piece : this) {
      if (piece.getSide() != side) {
        continue;
      }
      Position from = piece.getPosition();
      for (Move move : getLegalMoves(from)) {
        moves.add(move);
      }
    }
    return moves;
  }

  /**
   * Get piece stored at position.
   *
   * @param position of piece to retrieve.
   * @return Piece stored at position.
   */
  public Optional<Piece> getPiece(Position position) {
    return board.getPiece(position);
  }

  /**
   * Get pieces with side.
   *
   * @param side of pieces to retrieve.
   * @return List of pieces with side on board.
   */
  protected List<Piece> getPieces(Side side) {
    List<Piece> pieces = new ArrayList<>();
    for (Position position : board) {
      Piece piece = getPiece(position).get();
      if (piece.getSide() == side) {
        pieces.add(piece);
      }
    }
    return pieces;
  }

  /**
   * Get pieces with side and piece type.
   *
   * @param side of pieces to retrieve.
   * @param type of pieces to retrieve.
   * @return List of pieces with side and piece type on board.
   */
  protected List<Piece> getPieces(String type, Side side) {
    List<Piece> pieces = getPieces(side);
    return pieces.stream().filter(p -> p.getType().equals(type)).collect(Collectors.toList());
  }

  /**
   * Attempts a move; piece from position start to position end.
   *
   * @param start position of piece to move.
   * @param end position of piece to move.
   * @return Optional<Move> of performed move.
   * @see Move
   */
  public Optional<Move> movePiece(Position start, Position end, Boolean sound) {
    if (start.equals(end) || !board.insideBounds(end) || !board.insideBounds(start)) {
      return Optional.empty();
    }

    Optional<Piece> optionalPiece = board.getPiece(start);
    if (optionalPiece.isEmpty()) {
      return Optional.empty();
    }

    Piece piece = optionalPiece.get();
    boolean playingSide = piece.getSide().equals(side);
    if (!playingSide || !piece.legalMoveEnd(end)) {
      return Optional.empty();
    }

    Optional<Move> move =
        Optional.of(new Move(start, board.getPiece(start).get(), end, board.getPiece(end)));
    boolean moved = board.movePiece(start, end);

    if (moved) {
      promotePawnToQueen(end);
      changeSide();
      if (sound) {
        SoundPlayer.playSound("move.wav");
      }
      return move;
    }
    return Optional.empty();
  }

  /**
   * Undos a move if the move was legal in the first place.
   *
   * @param move Move to undo.
   * @param sound To play a sound during the undoing.
   * @return Move undone or not.
   */
  public boolean undoMove(Move move, Boolean sound) {
    // Force undo move.
    board.removePiece(move.start());
    board.removePiece(move.end());
    board.placePiece(move.start(), move.moving());
    if (move.target().isPresent()) {
      board.placePiece(move.end(), move.target().get());
    }

    // If move wasn't legal to perform, just don't undo the move.
    if (!this.getLegalMoves(move.start()).contains(move)) {
      board.removePiece(move.start());
      board.removePiece(move.end());
      board.placePiece(move.end(), move.moving());
      if (move.target().isPresent()) {
        board.placePiece(move.start(), move.target().get());
      }
      return false;
    }

    if (sound) {
      SoundPlayer.playSound("move.wav");
    }
    return true;
  }

  /**
   * Gets if a side's king piece is in check.
   *
   * @param side to get if king is in check of.
   * @return if side's king is in check or not.
   */
  public boolean kingInCheck(Side side) {
    List<Piece> kings = getPieces("K", side);
    if (kings.isEmpty()) {
      return false;
    }

    Piece king = kings.get(0);
    for (Position position : board) {
      if (board.getPiece(position).isEmpty()) {
        continue;
      }

      Piece piece = board.getPiece(position).get();
      if (piece.getSide() == side) {
        continue;
      }
      ;

      if (piece.movementPattern(king.getPosition())) {
        return true;
      }
    }

    return false;
  }

  /**
   * Gets if game is over. Game is over if a side has no available moves to perform.
   *
   * @return true if game is over, false else.
   */
  public boolean gameOver(Boolean sound) {
    List<Move> moveEndingsWhite = new ArrayList<>();
    List<Move> moveEndingsBlack = new ArrayList<>();

    for (Piece piece : this) {
      if (piece.getSide() == Side.WHITE) {
        moveEndingsWhite.addAll(getLegalMoves(piece.getPosition()));
        continue;
      }
      moveEndingsBlack.addAll(getLegalMoves(piece.getPosition()));
    }

    if (moveEndingsBlack.isEmpty() || moveEndingsWhite.isEmpty()) {
      if (sound) {
        SoundPlayer.playSound("checkmate.wav");
      }
      return true;
    }
    return false;
  }

  /**
   * Attempts a promotion of pawn to queen.
   *
   * @param position of pawn
   * @return true if piece got promoted to queen, false else.
   */
  protected boolean promotePawnToQueen(Position position) {
    if (!board.insideBounds(position)) {
      return false;
    }

    Optional<Piece> pieceOpt = board.getPiece(position);
    if (pieceOpt.isEmpty()) {
      return false;
    }

    Piece piece = pieceOpt.get();
    boolean pawnPieceMoved = piece.getType().equals("P");
    boolean onEndRow =
        (piece.getSide() == Side.WHITE
            ? piece.getPosition().y() == board.getHeight() - 1
            : piece.getPosition().y() == 0);

    if (pawnPieceMoved && onEndRow) {
      board.removePiece(position);
      new QueenPiece(this, position, piece.getSide());
      return true;
    }
    return false;
  }

  /**
   * Gets value of board relative to side. Positively sums up all pieces of side, and negatively
   * sums up other side's pieces. Gives a rough metric of advantage of side over the other side.
   *
   * @param side of metric
   * @return sum of all piece values of side and negative sum of other side's piece values.
   * @see getValue() in ChessPiece
   */
  public double getBoardValue(Side side) {
    double sum = 0d;
    for (Piece piece : this) {
      sum += piece.getSide() == side ? piece.getValue() : -piece.getValue();
    }
    return sum;
  }

  @Override
  public Iterator<Piece> iterator() {
    List<Piece> pieces = new ArrayList<>();
    for (Position position : board) {
      Optional<Piece> piece = board.getPiece(position);
      if (piece.isPresent()) {
        pieces.add(piece.get());
      }
    }
    return pieces.iterator();
  }

  /**
   * (non-Javadoc) Create a deep copy of the board.
   *
   * @see java.lang.Object#clone()
   */
  public Board clone() {
    Board copy = emptyBoard();
    copy.side = this.side;
    for (Piece p : this) {
      switch (p.getType()) {
        case "P" -> new PawnPiece(copy, p.getPosition(), p.getSide());
        case "R" -> new RookPiece(copy, p.getPosition(), p.getSide());
        case "N" -> new KnightPiece(copy, p.getPosition(), p.getSide());
        case "B" -> new BishopPiece(copy, p.getPosition(), p.getSide());
        case "Q" -> new QueenPiece(copy, p.getPosition(), p.getSide());
        case "K" -> new KingPiece(copy, p.getPosition(), p.getSide());
        default -> {}
      }
    }
    return copy;
  }
}
