# Searching-Github-User-with-Pagination

Fetch Data from Github API with Pagination - MVVM Kotlin

Folders :

- adapter : set the data that retrieved from service to recyclerview 
- api : configuration to called the service, query to search GitHub users and type of request like POST, GET, PUT, etc.
- data : filter retrieved data and pagination for the result
- di : configuration of dependency injection
- model : models to retrieve data from JSON returned by the server, handling success and error
- view : declaration UI and components for constructing Search Page
- viewModel : handle LiveData for inputed query and result from service

Libraries

- Android Support
- Android Architecture Components
- Coroutines
- Retrofit
- Okhttp
- Glide

Don't forget to handle API limit error (only 10 search request / minute). Check it out https://docs.github.com/en/rest#rate-limiting.
