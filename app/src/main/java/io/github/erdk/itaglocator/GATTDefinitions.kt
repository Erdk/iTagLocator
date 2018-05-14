package io.github.erdk.itaglocator

import java.util.HashMap

class GATTDefinitions {

    private val btUuids = HashMap<String, String>()

    init {
        // GATT Services
        btUuids["00001800-0000-1000-8000-00805f9b34fb"] = "Generic Access"
        btUuids["00001801-0000-1000-8000-00805f9b34fb"] = "Generic Attribute"
        btUuids["00001802-0000-1000-8000-00805f9b34fb"] = "Immediate Alert"
        btUuids["00001803-0000-1000-8000-00805f9b34fb"] = "Link Loss"
        btUuids["00001804-0000-1000-8000-00805f9b34fb"] = "Tx Power"
        btUuids["00001805-0000-1000-8000-00805f9b34fb"] = "Current Time Service"
        btUuids["00001806-0000-1000-8000-00805f9b34fb"] = "Reference Time Update Service"
        btUuids["00001807-0000-1000-8000-00805f9b34fb"] = "Next DST Change Service"
        btUuids["00001808-0000-1000-8000-00805f9b34fb"] = "Glucose"
        btUuids["00001809-0000-1000-8000-00805f9b34fb"] = "Health Thermometer"
        btUuids["0000180a-0000-1000-8000-00805f9b34fb"] = "Device Information Service"
        btUuids["0000180d-0000-1000-8000-00805f9b34fb"] = "Heart Rate"
        btUuids["0000180e-0000-1000-8000-00805f9b34fb"] = "Phone Alert Status Service"
        btUuids["0000180f-0000-1000-8000-00805f9b34fb"] = "Battery Service"
        btUuids["00001810-0000-1000-8000-00805f9b34fb"] = "Blood Pressure"

        btUuids["00001811-0000-1000-8000-00805f9b34fb"] = "Alert Notification Service"
        btUuids["00001812-0000-1000-8000-00805f9b34fb"] = "Human Interface Device"
        btUuids["00001813-0000-1000-8000-00805f9b34fb"] = "Scan Parameters"
        btUuids["00001814-0000-1000-8000-00805f9b34fb"] = "Running Speed and Cadence"
        btUuids["00001815-0000-1000-8000-00805f9b34fb"] = "Automation IO"
        btUuids["00001816-0000-1000-8000-00805f9b34fb"] = "Cycling Speed and Cadence"
        btUuids["00001818-0000-1000-8000-00805f9b34fb"] = "Cycling Power"
        btUuids["00001819-0000-1000-8000-00805f9b34fb"] = "Location and Navigation"
        btUuids["0000181a-0000-1000-8000-00805f9b34fb"] = "Environmental Sensing"
        btUuids["0000181b-0000-1000-8000-00805f9b34fb"] = "Body Composition"
        btUuids["0000181c-0000-1000-8000-00805f9b34fb"] = "User Data"
        btUuids["0000181d-0000-1000-8000-00805f9b34fb"] = "Weight Scale"
        btUuids["0000181e-0000-1000-8000-00805f9b34fb"] = "Bond Management Service"
        btUuids["0000181f-0000-1000-8000-00805f9b34fb"] = "Continuous Glucose Monitoring"

        btUuids["00001820-0000-1000-8000-00805f9b34fb"] = "Internet Protocol Support Service"
        btUuids["00001822-0000-1000-8000-00805f9b34fb"] = "Pulse Oximeter Service"
        btUuids["00001823-0000-1000-8000-00805f9b34fb"] = "HTTP Proxy"
        btUuids["00001824-0000-1000-8000-00805f9b34fb"] = "Transport Discovery"
        btUuids["00001825-0000-1000-8000-00805f9b34fb"] = "Object Transfer Service"
        btUuids["00001826-0000-1000-8000-00805f9b34fb"] = "Fitness Machine"
        btUuids["00001827-0000-1000-8000-00805f9b34fb"] = "Mesh Provisioning Service"
        btUuids["00001828-0000-1000-8000-00805f9b34fb"] = "Mesh Proxy Service"
        btUuids["00001829-0000-1000-8000-00805f9b34fb"] = "Reconnection Configuration"

        // 180a - Device Information
        btUuids["00002a23-0000-1000-8000-00805f9b34fb"] = "System ID"
        btUuids["00002a24-0000-1000-8000-00805f9b34fb"] = "Model Name"
        btUuids["00002a25-0000-1000-8000-00805f9b34fb"] = "Serial Number"
        btUuids["00002a26-0000-1000-8000-00805f9b34fb"] = "Firmware Revision"
        btUuids["00002a27-0000-1000-8000-00805f9b34fb"] = "Hardware Revision"
        btUuids["00002a28-0000-1000-8000-00805f9b34fb"] = "Software Revision"
        btUuids["00002a29-0000-1000-8000-00805f9b34fb"] = "Manufacturer Name"
        btUuids["00002a2a-0000-1000-8000-00805f9b34fb"] = "IEEE 11073-20601 Regulatory Certification Data List"
        btUuids["00002a50-0000-1000-8000-00805f9b34fb"] = "PnP ID"

        // 1802 - Immediate Alert
        // 1803 - Link Loss
        btUuids["00002a06-0000-1000-8000-00805f9b34fb"] = "Alert Level"
    }

    fun uuidToName(uuid: String, defaultName: String): String {
        return btUuids[uuid] ?: defaultName
    }
}