package nl.kega.newland;

import java.util.HashMap;
import java.util.Map;

/**
 * Newland Barcode Symbology ID Number mapping
 * Maps numeric symbology IDs to human-readable barcode type names
 */
public class BarcodeTypes {
      private static final Map<Integer, String> BARCODE_TYPE_MAP = new HashMap<Integer, String>() {{
        // Official Newland Symbology ID Mapping (0-65535)
        
        // Setup and Configuration Codes (0-1)
        put(0, "ZASETUP");
        put(1, "SETUP128");
        
        // 1D Linear Barcodes (2-45)
        put(2, "CODE128");
        put(3, "UCCEAN128");
        put(4, "AIM128");
        put(5, "GS1_128");
        put(6, "ISBT128");
        put(7, "EAN8");
        put(8, "EAN13");
        put(9, "UPCE");
        put(10, "UPCA");
        put(11, "ISBN");
        put(12, "ISSN");
        put(13, "CODE39");
        put(14, "CODE93");
        put(15, "93I");
        put(16, "CODABAR");
        put(17, "ITF");
        put(18, "ITF6");
        put(19, "ITF14");
        put(20, "DPLEITCODE");
        put(21, "DPIDENTCODE");
        put(22, "CHNPOST25");
        put(23, "STANDARD25");  // Note: Also IATA25 according to docs
        put(24, "MATRIX25");
        put(25, "INDUSTRIAL25");
        put(26, "COOP25");
        put(27, "CODE11");
        put(28, "MSIPLESSEY");
        put(29, "PLESSEY");
        put(30, "RSS14");
        put(31, "RSSLIMITED");
        put(32, "RSSEXPANDED");
        put(33, "TELEPEN");
        put(34, "CHANNELCODE");
        put(35, "CODE32");
        put(36, "CODEZ");
        put(37, "CODABLOCKF");
        put(38, "CODABLOCKA");
        put(39, "CODE49");
        put(40, "CODE16K");
        put(41, "HIBC128");
        put(42, "HIBC39");
        put(43, "RSSFAMILY");
        put(44, "TriopticCODE39");
        put(45, "UPC_E1");
        
        // 2D Matrix Barcodes (256-274)
        put(256, "PDF417");
        put(257, "MICROPDF");
        put(258, "QRCODE");
        put(259, "MICROQR");
        put(260, "AZTEC");
        put(261, "DATAMATRIX");
        put(262, "MAXICODE");
        put(263, "CSCODE");
        put(264, "GRIDMATRIX");
        put(265, "EARMARK");
        put(266, "VERICODE");
        put(267, "CCA");
        put(268, "CCB");
        put(269, "CCC");
        put(270, "COMPOSITE");
        put(271, "HIBCAZT");
        put(272, "HIBCDM");
        put(273, "HIBCMICROPDF");
        put(274, "HIBCQR");
        
        // Postal Barcodes (512-520)
        put(512, "POSTNET");
        put(513, "ONECODE");
        put(514, "RM4SCC");
        put(515, "PLANET");
        put(516, "KIX");
        put(517, "APCUSTOM");
        put(518, "APREDIRECT");
        put(519, "APREPLYPAID");
        put(520, "APROUTING");
        
        // OCR and Document Codes (768-770)
        put(768, "NUMOCRB");
        put(769, "PASSPORT");
        put(770, "TD1");
        
        // Private and Custom Codes (2048-2049)
        put(2048, "PRIVATE");
        put(2049, "ZZCODE");
        
        // Unknown/Default (65535)
        put(65535, "UNKNOWN");
    }};
    
    /**
     * Get the human-readable barcode type name from the numeric symbology ID
     * @param typeId The numeric symbology ID from Newland scanner
     * @return Human-readable barcode type name, or "Unknown (typeId)" if not found
     */
    public static String getBarcodeTypeName(int typeId) {
        return BARCODE_TYPE_MAP.getOrDefault(typeId, "Unknown (" + typeId + ")");
    }
    
    /**
     * Check if a symbology ID is supported
     * @param typeId The numeric symbology ID
     * @return true if the symbology is known, false otherwise
     */
    public static boolean isKnownType(int typeId) {
        return BARCODE_TYPE_MAP.containsKey(typeId);
    }
    
    /**
     * Get all supported barcode types
     * @return Map of all supported symbology IDs and their names
     */
    public static Map<Integer, String> getAllBarcodeTypes() {
        return new HashMap<>(BARCODE_TYPE_MAP);
    }
      /**
     * All valid Newland CODE_ID values for barcode configuration
     */
    public static final String[] VALID_CODE_IDS = {
        // 1D Linear Barcodes
        "CODE128",     // Code 128
        "UCCEAN128",   // GS1-128 (UCC/EAN-128)
        "AIM128",      // AIM 128
        "EAN8",        // EAN-8
        "EAN13",       // EAN-13
        "ISSN",        // ISSN
        "ISBN",        // ISBN
        "UPCE",        // UPC-E
        "UPCA",        // UPC-A
        "ITF",         // Interleaved 2 of 5
        "ITF6",        // ITF-6
        "ITF14",       // ITF-14
        "MATRIX25",    // Matrix 2 of 5
        "IND25",       // Industrial 25
        "STD25",       // Standard 25
        "CODE39",      // Code 39
        "CODABAR",     // Codabar
        "CODE93",      // Code 93
        "CODE11",      // Code 11
        "PLSY",        // Plessey
        "MSIPLSY",     // MSI-Plessey
        "RSS",         // RSS

        // 2D Matrix Barcodes
        "PDF417",      // PDF417
        "QR",          // QR Code
        "DM",          // Data Matrix
        "CSC",         // Chinese Sensible (CS) Code
        "AZTEC",       // Aztec
        "MICROPDF",    // MicroPDF417
        "MICROQR"      // Micro QR
    };
    
    /**
     * Validates if a CODE_ID is supported by Newland devices
     * @param codeId The CODE_ID string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidCodeId(String codeId) {
        if (codeId == null || codeId.trim().isEmpty()) {
            return false;
        }

        String typeToCheck = codeId.trim().toUpperCase();
        
        for (String validType : VALID_CODE_IDS) {
            if (validType.equals(typeToCheck)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Get barcode category based on type ID
     * @param typeId The numeric symbology ID
     * @return Category string (1D, 2D, Postal, etc.)
     */
    public static String getBarcodeCategory(int typeId) {
        // Setup and Configuration Codes
        if (typeId >= 0 && typeId <= 1) return "Setup";
        
        // 1D Linear Barcodes
        if (typeId >= 2 && typeId <= 45) return "1D Linear";
        
        // 2D Matrix Barcodes
        if (typeId >= 256 && typeId <= 274) return "2D Matrix";
        
        // Postal Barcodes
        if (typeId >= 512 && typeId <= 520) return "Postal";
        
        // OCR and Document Codes
        if (typeId >= 768 && typeId <= 770) return "OCR/Document";
        
        // Private and Custom Codes
        if (typeId >= 2048 && typeId <= 2049) return "Private/Custom";
        
        // Unknown/Default
        if (typeId == 65535) return "Unknown";
        
        return "Other";
    }
}
