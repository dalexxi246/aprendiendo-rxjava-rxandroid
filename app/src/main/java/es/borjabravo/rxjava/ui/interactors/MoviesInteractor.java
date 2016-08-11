package es.borjabravo.rxjava.ui.interactors;

import es.borjabravo.rxjava.Api;
import es.borjabravo.rxjava.io.callbacks.MoviesCallback;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

// TODO Fuente: http://katade.com/programacion-reactiva-en-android-rxjava/
// TODO Pulled From: https://github.com/borjabravo10/RxJava
public class MoviesInteractor {

    private final String TAG = getClass().getName();

    Api api;

    public MoviesInteractor(Api api) {
        this.api = api;
    }

    /*
    public void getMoviesFromSearch(final MoviesCallback callback) {
        api.getMoviesFromSearch()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<MoviesResponse>() {
                @Override
                public void onCompleted() {
                    callback.onCompleted();
                }

                @Override
                public void onError(Throwable e) {
                    callback.onError();
                }

                @Override
                public void onNext(MoviesResponse moviesResponse) {
                    callback.onMoviesFound(moviesResponse.getMovies());
                }
            });
    }
     */

    //Simple request - All movies from organization choosen
    public void getMoviesFromSearch(final MoviesCallback callback) {
        api.getMoviesFromSearch()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(moviesResponse -> callback.onMoviesFound(moviesResponse.getMovies()),
                        error -> callback.onError(),
                        callback::onCompleted);
    }

    /*
    public void getMoviesFilteredFromSearch(final MoviesCallback callback) {
        api.getMoviesFromSearch()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap(new Func1<MoviesResponse, Observable<Movie>>() {
                @Override
                public Observable<Movie> call(MoviesResponse moviesResponse) {
                    return Observable.from(moviesResponse.getMovies());
                }
            })
            .filter(new Func1<Movie, Boolean>() {
                @Override
                public Boolean call(Movie movie) {
                    return movie.getType().equalsIgnoreCase("movie");
                }
            })
            .subscribe(new Subscriber<Movie>() {
                @Override
                public void onCompleted() {
                    callback.onCompleted();
                }

                @Override
                public void onError(Throwable e) {
                    callback.onError();
                }

                @Override
                public void onNext(Movie movie) {
                    callback.onMovieFound(movie);
                }
            });
    }
     */
    //Simple request - All movies from organization choosen - Filtered by only movies
    public void getMoviesFilteredFromSearch(final MoviesCallback callback) {
        api.getMoviesFromSearch()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(moviesResponse -> Observable.from(moviesResponse.getMovies()))
                .filter(movie -> movie.getType().equalsIgnoreCase("movie"))
                .subscribe(callback::onMovieFound,
                        error -> callback.onError(),
                        callback::onCompleted);
    }

    /*
    public void getMovieInfo(final MoviesCallback callback) {
    api.getMoviesFromSearch()
        .flatMap(new Func1<MoviesResponse, Observable<Movie>>() {
            @Override
            public Observable<Movie> call(MoviesResponse moviesResponse) {
                return Observable.from(moviesResponse.getMovies());
            }
        })
        .flatMap(new Func1<Movie, Observable<Movie>>() {
            @Override
            public Observable<Movie> call(Movie movie) {
                return api.getMovieInfo(movie.getImdbID());
            }
        })
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<Movie>() {
            @Override
            public void onCompleted() {
                callback.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                callback.onError();
            }

            @Override
            public void onNext(Movie movie) {
                callback.onMovieFound(movie);
            }
        });
}
     */
    //Nested request - Movie detail info
    public void getMovieInfo(final MoviesCallback callback) {
        api.getMoviesFromSearch()
                .flatMap(moviesResponse -> Observable.from(moviesResponse.getMovies()))
                .flatMap(movie -> api.getMovieInfo(movie.getImdbID()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onMovieFound,
                        error -> callback.onError(),
                        callback::onCompleted);
    }
}