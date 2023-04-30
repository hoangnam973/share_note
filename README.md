* Share Note - MVVM Architecture

Share Note is a sample project that is allow users to save a note and retrieve notes in Google Firebase database.
With some functions below:
1. As a user (of the application) I can set my username.
2. As a user I can save a short note on Firebase database.
3. As a user I can see a list of my saved notes.
4. As a user I can see all the notes from other users.

* Description

+ Modern Architecture
    - Single activity architecture using Navigation component
    - ViewModel
    - MVVM for presentation layer
    - Jetpack Kotlin extensions

* UI 
   - Jetpack Compose for UI
   - Material design

* Tech/Tools
    - Kotlin
    - Coroutines and Flow for async operations
    - Hilt for dependency injection
    - Firebase Realtime Database for read and write data
    - Jetpack
        . Compose 
        . Navigation for navigation between composables
        . ViewModel that stores, exposes and manages UI state


* Explain more
    * View: Composable screens that consume state, apply effects and delegate events upstream.
    * ViewModel: that manages and set the state of the corresponding screen. Additionally, it intercepts UI events as callbacks and produces side-effects. The ViewModel is scoped to the lifetime of the corresponding screen composable in the backstack.
    * Model: Data source classes that retrieve content.

* Two main components in the presentation layer
    * State: contains the status of the respective screens.
    * Effect: plain object that signals one-time side-effect actions that should impact the UI
    Every screen/flow defines its own contract class that states all corresponding core components described above: state content and effects.

* Dependency Injection
    * Most of the dependencies are injected with @Singleton scope
    * For ViewModels, we use the out-of-the-box @HiltViewModel annotation that injects them with the scope of the navigation graph composables that represent the screens.

* Compose
    * The project uses an EntryPointActivity that defines a navigation graph where every screen is a composable.
    * The EntryPointActivity also collects state objects and passes them together with the Effect flows to each Screen composable.

* Time I perform functions
    - Building structure: 4h
    - Login screen: 2h
    - Register account dialog: 2h
    - My list notes: 2h
    - All list notes: 2h
    - Create note dialog: 2h
# share_note
