import React, { useEffect, useState } from 'react';
import { View, Text, Button, StyleSheet, Alert, ScrollView } from 'react-native';
import { BarcodeScanner } from 'react-native-newland';

const BarcodeExample = () => {
  const [scannedData, setScannedData] = useState(null);
  const [isEnabled, setIsEnabled] = useState(false);
  const [scanHistory, setScanHistory] = useState([]);

  useEffect(() => {
    // Set up barcode event listeners
    const barcodeSubscription = BarcodeScanner.onBarcode((data) => {
      console.log('Barcode scanned:', data);
      setScannedData(data);
      
      // Add to history
      setScanHistory(prev => [data, ...prev.slice(0, 9)]); // Keep last 10 scans
        Alert.alert(
        'Barcode Scanned', 
        `Data: ${data.data}\nType: ${data.type}\nCategory: ${data.category}\nType ID: ${data.typeId}\nStatus: ${data.status}`
      );
    });

    // Cleanup subscription on unmount
    return () => {
      barcodeSubscription.remove();
      BarcodeScanner.release();
    };
  }, []);

  const handleEnable = () => {
    BarcodeScanner.enable();
    setIsEnabled(true);
  };

  const handleDisable = () => {
    BarcodeScanner.disable();
    setIsEnabled(false);
  };

  const handleScan = () => {
    BarcodeScanner.read({
      // Optional configuration
      timeout: 30000, // 30 seconds timeout
      continuous: false // Single scan mode
    });
  };

  const formatTimestamp = (timestamp) => {
    return new Date(parseInt(timestamp)).toLocaleString();
  };
  return (
    <ScrollView style={styles.container}>
      <Text style={styles.title}>Newland Barcode Scanner</Text>
      
      <View style={styles.buttonContainer}>
        <Button 
          title={isEnabled ? "Disable Scanner" : "Enable Scanner"}
          onPress={isEnabled ? handleDisable : handleEnable}
        />
      </View>

      <View style={styles.buttonContainer}>
        <Button 
          title="Scan Barcode"
          onPress={handleScan}
          disabled={!isEnabled}
        />
      </View>

      {scannedData && (
        <View style={styles.resultContainer}>          <Text style={styles.resultTitle}>Last Scanned:</Text>
          <Text style={styles.resultText}>Data: {scannedData.data}</Text>
          <Text style={styles.resultText}>Type: {scannedData.type}</Text>
          <Text style={styles.resultText}>Category: {scannedData.category}</Text>
          <Text style={styles.resultText}>Type ID: {scannedData.typeId}</Text>
          <Text style={styles.resultText}>Status: {scannedData.status}</Text>
          <Text style={styles.resultText}>Time: {formatTimestamp(scannedData.timestamp)}</Text>
        </View>
      )}

      {scanHistory.length > 0 && (
        <View style={styles.historyContainer}>
          <Text style={styles.historyTitle}>Scan History:</Text>
          {scanHistory.map((scan, index) => (            <View key={index} style={styles.historyItem}>
              <Text style={styles.historyData}>{scan.data}</Text>
              <Text style={styles.historyType}>{scan.type} ({scan.category})</Text>
              <Text style={styles.historyType}>ID: {scan.typeId}</Text>
              <Text style={styles.historyTime}>{formatTimestamp(scan.timestamp)}</Text>
            </View>
          ))}
        </View>
      )}
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    textAlign: 'center',
    marginBottom: 30,
  },
  buttonContainer: {
    marginVertical: 10,
  },
  resultContainer: {
    marginTop: 30,
    padding: 15,
    backgroundColor: '#e8f5e8',
    borderRadius: 8,
    borderWidth: 1,
    borderColor: '#4caf50',
  },
  resultTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 10,
    color: '#2e7d32',
  },
  resultText: {
    fontSize: 16,
    marginVertical: 2,
    color: '#333',
  },
  historyContainer: {
    marginTop: 20,
    padding: 15,
    backgroundColor: '#f5f5f5',
    borderRadius: 8,
  },
  historyTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 10,
    color: '#333',
  },
  historyItem: {
    padding: 10,
    marginVertical: 2,
    backgroundColor: '#fff',
    borderRadius: 4,
    borderLeftWidth: 3,
    borderLeftColor: '#2196f3',
  },
  historyData: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#333',
  },
  historyType: {
    fontSize: 14,
    color: '#666',
  },
  historyTime: {
    fontSize: 12,
    color: '#999',
  },
});

export default BarcodeExample;
