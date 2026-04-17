# Android Version (WIP)

This directory contains the Android port skeleton for the SIFT map tracker.

## Current Pipeline
1. MainActivity requests screen-capture permission
2. Capture session is stored in memory
3. TrackingService starts a foreground loop
4. OverlayService shows live text on screen
5. FakeLocator currently simulates coordinates

## Existing Modules
- `capture/ScreenCaptureManager.kt`: permission entry for MediaProjection
- `capture/ScreenImageProvider.kt`: placeholder for real Bitmap capture
- `capture/MiniMapCropper.kt`: minimap crop helper
- `capture/FakeLocator.kt`: fake coordinates for pipeline validation
- `match/SiftMatcher.kt`: placeholder for OpenCV SIFT matching
- `TrackingService.kt`: tracking loop and broadcast output
- `OverlayService.kt`: floating text overlay

## Next Real Integration Steps
### 1. Real screen capture
Replace `ScreenImageProvider.getLatestFrame()` with:
- MediaProjection
- VirtualDisplay
- ImageReader
- Bitmap conversion

### 2. Real coordinate extraction
In `TrackingService`, replace `FakeLocator.nextCoordinateText()` with:
- `val frame = screenImageProvider.getLatestFrame()`
- `val minimap = MiniMapCropper.crop(frame)`
- `val xy = siftMatcher.match(minimap)`

### 3. OpenCV integration
Recommended approaches:
- import OpenCV Android SDK as a local module, or
- load native libs from `jniLibs/`

### 4. Required Android permissions / behavior
- Foreground service
- Overlay permission
- Screen capture permission via MediaProjection

## Current Goal
The Android side is now a working pipeline skeleton. The remaining work is to replace fake data with real image input and OpenCV matching.
