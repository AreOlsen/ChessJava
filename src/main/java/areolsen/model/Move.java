package areolsen.model;

import areolsen.model.grid.Position;
import java.util.Optional;

/**
 * Chessmove.
 *
 * @param start position of piece to move
 * @param moving piece to move
 * @param end captured piece position
 * @param target optional piece to capture
 */
public record Move(Position start, Piece moving, Position end, Optional<Piece> target) {}
