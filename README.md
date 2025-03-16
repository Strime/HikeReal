# Hiking Tracker App

HikeReal is a modern Android application that allows hikers to capture, document, and share their mountain experiences in an authentic way. Unlike other hiking apps that focus solely on metrics, HikeReal emphasizes real moments and experiences during outdoor adventures.

## App Description

HikeReal consists of three main sections:

### 1. Feed
- View recent hikes from the community
- Explore hikes from around the world with their associated photos and metrics
- Discover the unique "DualView" feature that simultaneously captures the landscape (back) and the hiker (front)
- View detailed information such as elevation gain, distance, duration, and group size
- Vertical scrolling through hikes one at a time for an immersive experience

### 2. Live & Activities
- Follow ongoing hikes from other users in real-time
- Start your own hike and share it live with the community
- **Unique Activity Validation**: A hike can only be validated by taking at least one "DualView" photo during the activity
- The DualView technology captures both what you see and your reaction simultaneously

### 3. Profile
- View your hiking history and achievements
- Display your badges and accomplishments
- Track your personal statistics (total elevation gain, total distance, etc.)

## Architecture

The app follows a Model-View-ViewModel (MVVM) architecture:

* **Entity:** Represents the hiking data and data access objects (DAOs).
* **Model:** Represents the hiking data used by views.
* **View:** Jetpack Compose functions that display the UI and handle user interactions.
* **ViewModel:** Manages the UI state, interacts with the repository, and provides data to the View.

## Libraries and Components Used

* **Jetpack Compose:** For building the modern UI.
* **ViewModel:** For managing UI state.
* **Room:** For local data persistence.
* **Hilt:** For dependency injection.
* **CameraX:** For DualView photo capturing functionality.
* **Navigation Compose:** For navigating between different screens in the app.
* **Coroutines:** For asynchronous operations.
* **Flow:** For reactive programming and observing data changes.
* **Location Services:** For tracking user's position during hikes.
* **JUnit:** For unit testing.
* **MockK:** For creating mocks and stubs in unit tests.

## Features

* Vertical display of hikes one by one in the feed
* DualView functionality to capture landscape and hiker simultaneously
* Interactive visualization with movable Picture-in-Picture (PIP)
* Detailed metrics for each hike
* Support for dark/light mode
* Group size indicator (solo or number of people)
* Internationalization (i18n) with multi-language support

## Upcoming Features

* Location service during hike startup
* Adding friends for social features
* Team creation for "col quest" feature
* Map visualization with mountain passes owned by teams

## Running the App

1. Clone the repository.
2. Open the project in Android Studio.
3. Build and run the app on an emulator or device.
4. Note: The app requires camera permission to function properly. 

## Testing

The app includes a comprehensive testing suite to ensure reliability and functionality:

* **Unit Tests:** Located in the `src/test/java` directory.
* **Instrumented Tests:** Located in the `src/androidTest/java` directory.

Run tests from Android Studio or using the command line.

### Unit Tests
Located in the `src/test/java` directory, these tests verify individual components in isolation:
* **ViewModel Tests:** Verify state management and business logic
* **Repository Tests:** Ensure data operations work correctly
* 
### Test Factories
The app uses test factory classes to generate test data:
* **HikeEntityFactory:** Creates database entities for testing
* **HikePostFactory:** Generates domain models for UI testing

## Business Model

The business model and monetization strategy for HikeReal is available in the following documents:
- [HikeReal Business Model - EN.pdf](/business-model/HikeReal%20Business%20Model%20-%20EN.pdf) - English version
- [HikeReal Business Model - FR.pdf](/business-model/HikeReal%20Business%20Model%20-%20FR.pdf) - French version

## Screenshots

Located in the `screenshots` directory.

[<img src="screenshots/feed.png" width="200"/>](screenshots/feed.png)
[<img src="screenshots/new_hike.png" width="200"/>](screenshots/new_hike.png)
[<img src="screenshots/profile.png" width="200"/>](screenshots/profile.png)
[<img src="screenshots/feed_light.png" width="200"/>](screenshots/feed_light.png)
[<img src="screenshots/profile_light.png" width="200"/>](screenshots/profile_light.png)
[<img src="screenshots/camera.png" width="200"/>](screenshots/camera.png)
[<img src="screenshots/camera_choose.png" width="200"/>](screenshots/camera_choose.png)
[<img src="screenshots/new_feed.png" width="200"/>](screenshots/new_feed.png)


## License

```
Copyright 2025 Gaëtan S.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
