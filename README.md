# PopularMoviesAdvance

In this project, I added more functionality to the app I built in Stage 1 by building on the detail view for each movie, allowing users to 'favorite' movies.

functionality in stage 2 project:

* allow users to view and play trailers (either in the youtube app or a web browser).

* allow users to read reviews of a selected movie.

* also allow users to mark a movie as a favorite in the details view by tapping a button (heart).

* make use of Android Architecture Components (Room, LiveData, ViewModel and Lifecycle) to create a robust an efficient application.

* create a database using Room to store the names and ids of the user's favorite movies (and optionally, the rest of the information needed to display their favorites collection while offline).

* modify the existing sorting criteria for the main view to include an additional pivot to show their favorites collection.


Recall from Stage 1, you built a UI that presented the user with a grid of movie posters, allowed users to change sort order, and presented a screen with additional information on the movie selected by the user:


## Languages, libraries and tools used

* [Java](https://docs.oracle.com/javase/8/)
* Android Support Libraries
* [Picasso](https://github.com/square/picasso)
* [Butterknife](https://github.com/JakeWharton/butterknife)
* Room
* Retrofit
* ViewModel and LiveData


## How to build the app

1. Clone this repository in your local machine:

2. Open Android Studio and open the project from File > Open...

3. Get a developer API key from [The Movie Database](https://www.themoviedb.org/).

4. Then inside the gradle.properties file substitute API_KEY with your actual API key. Remember to not push your key to any public repository. API_KEY="YOUR_API_KEY"


https://user-images.githubusercontent.com/42872628/68806343-99c87d80-065d-11ea-985b-acd07a1f20ff.png

https://user-images.githubusercontent.com/42872628/68806451-d4321a80-065d-11ea-99e0-b8278d3d96a1.png

https://user-images.githubusercontent.com/42872628/68806478-e1e7a000-065d-11ea-93a5-f64066eaa305.png

https://user-images.githubusercontent.com/42872628/68806527-f88df700-065d-11ea-8886-17d7df8c1307.png
