# React Native Newland Barcode Scanner

A React Native library for integrating Newland barcode scanners with Android devices.

## Installation

```bash
npm install react-native-newland
```

### Android Setup

1. Add the package to your React Native project's `MainApplication.java`:

```java
import nl.kega.newland.NewlandPackage;

// In the getPackages() method:
@Override
protected List<ReactPackage> getPackages() {
    return Arrays.<ReactPackage>asList(
        new MainReactPackage(),
        new NewlandPackage() // Add this line
    );
}
```

2. Add permissions to your `android/app/src/main/AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.VIBRATE" />
<uses-permission android:name="android.permission.FLASHLIGHT" />
```

## Usage

### Basic Example

```javascript
import React, { useEffect, useState } from 'react';
import { BarcodeScanner } from 'react-native-newland';

const App = () => {
  const [scannedData, setScannedData] = useState(null);

  useEffect(() => {
    // Enable the scanner
    BarcodeScanner.enable();

    // Listen for barcode scan events
    const subscription = BarcodeScanner.onBarcode((data) => {
      console.log('Barcode scanned:', data);
      setScannedData(data);
    });

    // Cleanup on unmount
    return () => {
      subscription.remove();
      BarcodeScanner.release();
    };
  }, []);

  const handleScan = () => {
    BarcodeScanner.read();
  };

  return (
    // Your UI here
  );
};
```

## API Reference

### Methods

#### `BarcodeScanner.enable()`
Enables the barcode scanner and registers broadcast receivers.

#### `BarcodeScanner.disable()`
Disables the barcode scanner and unregisters broadcast receivers.

#### `BarcodeScanner.read(config?)`
Triggers a barcode scan operation.

**Parameters:**
- `config` (optional): Configuration object for the scan operation

#### `BarcodeScanner.release()`
Releases all resources and unregisters receivers. Call this when you're done using the scanner.

### Events

#### `BarcodeScanner.onBarcode(callback)`
Listens for successful barcode scan events.

**Callback Parameters:**
- `data.data`: The scanned barcode data
- `data.type`: The barcode type name (e.g., "QR Code", "Code128", "EAN13")
- `data.typeId`: The numeric barcode type ID from Newland symbology mapping
- `data.category`: The barcode category (e.g., "2D Matrix", "1D Linear", "Postal")
- `data.status`: The scan status from the device
- `data.timestamp`: Timestamp when the barcode was scanned

**Supported Barcode Types:**
The library supports all major barcode symbologies including:
- **1D Barcodes**: Code128, Code39, Code93, UPC-A, UPC-E, EAN-13, EAN-8, Codabar, Interleaved 2 of 5, MSI, Code11
- **2D Barcodes**: QR Code, Data Matrix, PDF417, Aztec, MaxiCode
- **Postal Codes**: POSTNET, Planet, Australian Post, UK Post, Japan Post
- **Composite Codes**: RSS-14, RSS Limited, RSS Expanded
- **Healthcare**: HIBC variants of major symbologies
- **And many more** (see source code for complete mapping)

**Returns:** Subscription object with a `remove()` method

#### `BarcodeScanner.onBarcodes(callback)`
Listens for multiple barcode scan events (if supported).

#### `BarcodeScanner.onBarcodeEvents(callback)`
Listens for status events from the scanner.

## Newland Device Compatibility

This library is designed to work with Newland Android PDA devices that support the following intent actions:

- `nlscan.action.SCANNER_RESULT` - For receiving scan results
- `nlscan.action.SCANNER_TRIG` - For triggering scans

### Supported Newland Models

- Newland MT90 series
- Newland MT65 series
- Newland MT30 series
- Other Newland Android PDA devices with barcode scanning capabilities

## Troubleshooting

### Scanner Not Working

1. **Check Permissions**: Ensure camera and other required permissions are granted
2. **Device Compatibility**: Verify you're running on a Newland device
3. **Enable Scanner**: Make sure to call `BarcodeScanner.enable()` before scanning
4. **Check Logs**: Look for debug messages with tag "BarcodeModule"

### No Scan Results

1. **Event Listeners**: Ensure you've set up the `onBarcode` event listener
2. **Scanner State**: Check that the scanner is enabled
3. **Broadcast Intents**: Verify the device supports Newland broadcast intents

## Example

See the [example](./example/BarcodeExample.js) directory for a complete working example.

## License

ISC

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.
