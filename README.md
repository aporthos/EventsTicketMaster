# Events TicketMaster

### This app can be used to search events by country, save favorite events, view more details(dates, times, location, etc)

![screens](https://github.com/user-attachments/assets/cc8790d6-8cd7-4048-a6f5-c77b68ccdc34)

## Requirements

### Ticket Master API key

Events TicketMaster uses the [TicketMaster API](https://developer.ticketmaster.com/products-and-docs/apis/discovery-api/v2/) to load events. To use the API, you will need to obtain a free developer API key. See the
[TicketMaster API Documentation](https://developer.ticketmaster.com/products-and-docs/apis/getting-started/) for instructions.

Once you have the key, add this line to the `secret.properties` file, either in your user home
directory (usually `~/.gradle/gradle.properties` on Linux and Mac) or in the project's root folder:

```
API_KEY=<your TicletMaster api key>
```

<img src="https://github.com/user-attachments/assets/dc0a3a9d-aa2c-4793-9d93-2cf611b132bc" align="right" width="320"/>

## Tech stack & Open-source libraries
- Minimum SDK level 24.
- [Kotlin](https://kotlinlang.org/) based, utilizing [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous operations.
- Jetpack Libraries:
  - Jetpack Compose: Android’s modern toolkit for declarative UI development.
  - Lifecycle: Observes Android lifecycles and manages UI states upon lifecycle changes.
  - StateFlow - StateFlow is a state-holder observable flow that emits the current and new state updates to its collectors.
  - ViewModel: Manages UI-related data and is lifecycle-aware, ensuring data survival through configuration changes.
  - Navigation: Facilitates screen navigation, complemented by [Hilt Navigation Compose](https://developer.android.com/jetpack/compose/libraries#hilt) for dependency injection.
  - Room: Constructs a database with an SQLite abstraction layer for seamless database access.
  - Datastore: Jetpack DataStore is a data storage solution that allows you to store key-value pairs or typed objects with protocol buffers.
  - Paging: The Paging library helps you load and display pages of data from a larger dataset from local storage or over a network.
  - [Hilt](https://dagger.dev/hilt/): Facilitates dependency injection.
- Architecture:
  - MVVM Architecture (View - ViewModel - Model): Facilitates separation of concerns and promotes maintainability.
  - Repository Pattern: Acts as a mediator between different data sources and the application's business logic.
- [Retrofit2 & OkHttp3](https://github.com/square/retrofit): Constructs REST APIs and facilitates paging network data retrieval.
- [Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization): Kotlin multiplatform / multi-format reflectionless serialization.
- [Turbine](https://github.com/cashapp/turbine): A small testing library for kotlinx.coroutines Flow.
- [MockK](https://github.com/mockk/mockk): Allows mock objects in both your Android unit tests and instrumented tests.
- [Lottie](https://github.com/airbnb/lottie-android): Lottie is a mobile library for Android and iOS that parses Adobe After Effects animations exported as json with Bodymovin and renders them natively on mobile!.
- [Coil](https://github.com/cashapp/turbine): An image loading library for Android and Compose Multiplatform.
- [Moshi](https://github.com/square/moshi): Modern JSON library for Android, Java and Kotlin. It makes it easy to parse JSON into Java and Kotlin classes

# Architecture

The **Events TicketMaster** app follows the
[official architecture guidance](https://developer.android.com/topic/architecture) 
and is described in detail in the
[architecture learning journey](docs/ArchitectureLearningJourney.md).

![clean-arch](https://github.com/user-attachments/assets/572d5f9f-b429-4cbf-b756-189a6fed2887)


## Package Structure
![structure](https://github.com/user-attachments/assets/3569bdcc-2199-4d8a-be93-1c37dbbefd04)




