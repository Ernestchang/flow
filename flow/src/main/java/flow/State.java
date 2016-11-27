/*
 * Copyright 2016 Square Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package flow;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.View;

/**
 * contain view's state which through a key(obj) and viewState ,also have a bundle data to exchange and delivery
 */
public class State {
    /**
     * Creates a State instance that has no state and is effectively immutable.
     */
    @NonNull
    public static State empty(@NonNull final Object key) {
        return new EmptyState(key);
    }

    /**
     *  get a state obj from a bundle
     * @param savedState
     * @param parceler
     * @return
     */
    @NonNull
    static State fromBundle(@NonNull Bundle savedState, @NonNull KeyParceler parceler) {
        Object key = parceler.toKey(savedState.getParcelable("KEY"));
        State state = new State(key);
        state.viewState = savedState.getSparseParcelableArray("VIEW_STATE");
        state.bundle = savedState.getBundle("BUNDLE");
        return state;
    }

    private final Object key;
    @Nullable
    private Bundle bundle;
    // map integers to objects implements parcelable
    @Nullable
    SparseArray<Parcelable> viewState;

    State(Object key) {
        // No external instances.
        this.key = key;
    }

    /**
     * optional format obj
     * @param <T>
     * @return
     */
    @NonNull
    public final <T> T getKey() {
        @SuppressWarnings("unchecked") final T state = (T) key;
        return state;
    }

    public void save(@NonNull View view) {
        SparseArray<Parcelable> state = new SparseArray<>();

        // save this view hierarchy's frozen state into the given container
        view.saveHierarchyState(state);
        viewState = state;
    }

    public void restore(@NonNull View view) {
        if (viewState != null) {
            // restore the view hierarchy's frozen state from the given container
            view.restoreHierarchyState(viewState);
        }
    }

    public void setBundle(@Nullable Bundle bundle) {
        this.bundle = bundle;
    }


    @Nullable
    public Bundle getBundle() {
        return bundle;
    }

    /**
     *  return a bundle format data which can change to a state obj
     * @param parceler
     * @return
     */
    Bundle toBundle(KeyParceler parceler) {
        Bundle outState = new Bundle();
        outState.putParcelable("KEY", parceler.toParcelable(getKey()));
        if (viewState != null && viewState.size() > 0) {
            outState.putSparseParcelableArray("VIEW_STATE", viewState);
        }
        if (bundle != null && !bundle.isEmpty()) {
            outState.putBundle("BUNDLE", bundle);
        }
        return outState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return (getKey().equals(state.getKey()));
    }

    @Override
    public int hashCode() {
        return getKey().hashCode();
    }

    @Override
    public String toString() {
        return getKey().toString();
    }

    private static final class EmptyState extends State {
        public EmptyState(Object flowState) {
            super(flowState);
        }

        @Override
        public void save(@NonNull View view) {
        }

        @Override
        public void restore(@NonNull View view) {
        }

        @Override
        public void setBundle(Bundle bundle) {
        }

        @Nullable
        @Override
        public Bundle getBundle() {
            return null;
        }
    }
}
