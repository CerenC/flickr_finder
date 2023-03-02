# flickr_finder
FlickrFinder App contains two screens. First screen to show photo results of searched term in paginated list. When user clicks on an item in list, the full photo screen will be displayed on screen 

## Used tech-stack :
* Language - Kotlin
* Architecture - MVVM + data&domain&ui layers
* Architectural Components:
    * Paging-3
    * Navigation
    * ViewBinding
    * ViewModels
* Asynchronous operations - Coroutines
* Networking - Retrofit 2
* Json converter - Moshi
* Dependency Injection - Hilt
* Streaming - Flow, StateFlow
* Testing - Junit, Mockito

## Improvement points:
* Compose 
* Better test coverage 
* Better error handling with possible response error that determined on flicker api documents
* Idiomatic Gradle :https://github.com/jjohannes/idiomatic-gradle

## Helpful links:
* https://bladecoder.medium.com/kotlins-flow-in-viewmodels-it-s-complicated-556b472e281a
* https://medium.com/proandroiddev/using-livedata-flow-in-mvvm-part-i-a98fe06077a0
* https://github.com/android/architecture-components-samples
