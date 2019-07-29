package nz.co.warehouseandroidtest.di.scopes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * A custom scope stating the life-cycle of the annotated dependency that is Singleton to Fragment.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerFragment {
}