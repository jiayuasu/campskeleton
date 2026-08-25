package org.twak.utils.geom;

/**
 * Thrown by {@link LinearForm3D#collide(LinearForm3D, LinearForm3D)} when the three planes do not
 * meet in a single point: their normals are linearly dependent (which includes parallel or
 * coincident planes), or a plane has NaN coefficients. The skeleton machinery produces such configurations routinely for inputs with many
 * short, near-collinear edges (outlines traced from raster pixels, subdivided polygons), so callers
 * treat this as an expected, recoverable condition rather than a bug:
 *
 * <ul>
 * <li>{@code CoSitedCollision.validateChains} catches it locally and keeps the chain unchanged,
 * <li>{@code CollisionQ.cornerEdgeCollision} catches it and skips the candidate collision,
 * <li>{@code Skeleton.skeleton} catches whatever propagates out of an event and abandons that
 *     event,
 * <li>{@code Skeleton.capCopy} lets it propagate to its caller.
 * </ul>
 *
 * <p>This is deliberately an {@link Error} and not a {@link RuntimeException}. The original
 * Jama-based implementation threw {@code RuntimeException("Matrix is singular.")} here, which
 * activated parallel-edge fallbacks in {@code Edge.collide} and {@code Skeleton.capCopy}.
 * Restoring that behaviour was tried and measurably degraded the output on rasterised outlines
 * (skeleton edges far outside the input polygon, crossing arcs), so those fallbacks stay bypassed;
 * handling remains caller-specific as described above.
 */
public class SingularPlanesError extends Error
{
    public SingularPlanesError( String message )
    {
        super( message );
    }
}
