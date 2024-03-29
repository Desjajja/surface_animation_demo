package com.cyq.bluetooth

import android.Manifest
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.*
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.cyq.bluetooth.MyAdapter.OnItemClickListener
import com.cyq.common.ByteUtil
import com.cyq.common.LampBean
import com.cyq.common.LampView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        const val TAG = "BLE"

        //服务标识
        val UUID_SERVICE: UUID = UUID.fromString("38400000-8cf0-11bd-b23e-111111111111")

        //特征标识（读取数据）
        val UUID_READ_CHARACTERISTIC: UUID = UUID.fromString("38400000-8cf0-11bd-b23e-222222222222")

        //特征标识的描述（读取数据）
        val UUID_READ_DESCRIPTOR: UUID = UUID.fromString("38400000-8cf0-11bd-b23e-333333333333")

        //特征标识（写入数据）
        val UUID_WRITE_CHARACTERISTIC: UUID =
            UUID.fromString("38400000-8cf0-11bd-b23e-444444444444")

        //特征标识的描述（写入数据）
        val UUID_WRITE_DESCRIPTOR: UUID = UUID.fromString("38400000-8cf0-11bd-b23e-555555555555")
    }

    private val mPermission = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN
    )
    private lateinit var mBleAdapter: BluetoothAdapter
    private var mBluetoothDeviceList: MutableList<BluetoothDevice> = mutableListOf()
    private var mAdapter: MyAdapter? = null
    private var mBleService: BleService? = null
    private lateinit var mBluetoothGatt: BluetoothGatt
    private var mLampBean: LampBean = LampBean()

    private var mScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            val bluetoothDevice = result.device
            if (!TextUtils.isEmpty(bluetoothDevice.name) && !mBluetoothDeviceList.contains(
                    bluetoothDevice
                )
            ) {
                Log.d(TAG, "搜索到蓝牙设备：${bluetoothDevice.address}")
                mBluetoothDeviceList.add(bluetoothDevice)
                mAdapter?.notifyDataSetChanged()
            }
        }
    }

    var writeCharacteristic: BluetoothGattCharacteristic? = null
    var writeDescriptor: BluetoothGattDescriptor? = null
    var readCharacteristic: BluetoothGattCharacteristic? = null
    var readDescriptor: BluetoothGattDescriptor? = null

    private val mGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            Log.d(TAG, "回调onConnectionStateChange:" + gatt.toString())
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    //已连接,执行发现服务，当发现服务成功后回调BluetoothGattCallback.onServicesDiscovered()
                    Log.d(TAG, "gatt已连接，去发现服务")
                    mBluetoothGatt.discoverServices()
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    //断开连接
                    Log.d(TAG, "gatt链接已断开")
                }
            } else {
                //链接异常
                Log.d(TAG, "链接gatt服务异常")
            }
        }

        //搜索到服务的回调
        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            Log.d(TAG, "回调onServicesDiscovered:" + gatt.toString())

            if (status == BluetoothGatt.GATT_SUCCESS) {
                val gattService = mBluetoothGatt.getService(UUID_SERVICE)
                if (gattService != null) {
                    Log.d(TAG, "发现服务成功：" + gattService.uuid)
                    writeCharacteristic = gattService.getCharacteristic(UUID_WRITE_CHARACTERISTIC)
                    writeDescriptor = writeCharacteristic?.getDescriptor(UUID_WRITE_DESCRIPTOR)
                    readCharacteristic = gattService.getCharacteristic(UUID_READ_CHARACTERISTIC)
                    readDescriptor = readCharacteristic?.getDescriptor(UUID_READ_DESCRIPTOR)
                    Log.d(
                        TAG, "\nwriteCharacteristic:${writeCharacteristic?.uuid}\n" +
                                "writeDescriptor:${writeDescriptor?.uuid}\n" +
                                "readCharacteristic:${readCharacteristic?.uuid}\n" +
                                "readDescriptor:${readDescriptor?.uuid}\n"
                    )
                    /* val bytes: ByteArray = ByteUtil.toByteArray("00eeabcde1239879721731")
                     Log.d(TAG, ByteUtil.toHexString(bytes))
                     //通过gatt实体类写入值到特征类中
                     val result: Boolean? = writeCharacteristic?.setValue(bytes)
                     if (result == true) {
                         mBluetoothGatt.writeCharacteristic(writeCharacteristic)
                     }
                     //读取从机的特征值
                     mBluetoothGatt.readCharacteristic(readCharacteristic)*/
                } else {
                    Log.d(TAG, "发现服务失败！")
                }
            }
        }

        //当成功写入特征值时收到回调
        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            super.onCharacteristicWrite(gatt, characteristic, status)
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //获取写入到外设的特征值
                Log.d(TAG, "onCharacteristicWrite:${characteristic?.value}")
            }
        }

        //读取到特征值回调
        override fun onCharacteristicRead(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            super.onCharacteristicRead(gatt, characteristic, status)
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "读取到的特征值：${characteristic?.value}")
            }
        }

        //当成功写入特征描述值的时候回调
        override fun onDescriptorWrite(
            gatt: BluetoothGatt?,
            descriptor: BluetoothGattDescriptor?,
            status: Int
        ) {
            super.onDescriptorWrite(gatt, descriptor, status)
            Log.d(TAG, "onCharacteristicWrite:" + gatt.toString())
        }

        //当成功读取到特征描述值时触发
        override fun onDescriptorRead(
            gatt: BluetoothGatt?,
            descriptor: BluetoothGattDescriptor?,
            status: Int
        ) {
            super.onDescriptorRead(gatt, descriptor, status)
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "onDescriptorRead:${descriptor?.value}")
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?
        ) {
            super.onCharacteristicChanged(gatt, characteristic)
            Log.d(TAG, "onCharacteristicWrite:" + characteristic?.value)

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnSearchBLE.setOnClickListener(this)
        btnOpenLamp.setOnClickListener(this)
        btnCloseLamp.setOnClickListener(this)
        btnYellowLamp.setOnClickListener(this)
        btnGreenLamp.setOnClickListener(this)
        btnRedLamp.setOnClickListener(this)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = MyAdapter(mBluetoothDeviceList)
        mAdapter?.itemClickListener = object : OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                Toast.makeText(this@MainActivity, "开始连接", Toast.LENGTH_SHORT).show()
                mBleAdapter.bluetoothLeScanner.stopScan(mScanCallback)
                val bluetoothDevice = mBluetoothDeviceList[position]
                connectGatt(bluetoothDevice)
            }
        }
        mRecyclerView.adapter = mAdapter
        initBle()
    }

    private fun initBle() {
        ActivityCompat.requestPermissions(this, mPermission, 100)
        mBleAdapter = BluetoothAdapter.getDefaultAdapter()
        if (!mBleAdapter.isEnabled) {
            mBleAdapter.enable()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSearchBLE -> searchBleDevice()
            R.id.btnOpenLamp -> {
                lampView.open()
                mLampBean.openLamp()
            }
            R.id.btnCloseLamp -> {
                lampView.close()
                mLampBean.closeLamp()
            }
            R.id.btnGreenLamp -> {
                lampView.setLampColor(LampView.Color.GREEN)
                mLampBean.setColor(LampView.Color.GREEN)
            }
            R.id.btnYellowLamp -> {
                lampView.setLampColor(LampView.Color.YELLOW)
                mLampBean.setColor(LampView.Color.YELLOW)
            }
            R.id.btnRedLamp -> {
                lampView.setLampColor(LampView.Color.RED)
                mLampBean.setColor(LampView.Color.RED)
            }
        }
        if (v?.id != R.id.btnSearchBLE) {
            if (writeCharacteristic != null) {
                val bytes: ByteArray = ByteUtil.toByteArray(mLampBean.getCommandStr())
                Log.d(TAG, ByteUtil.toHexString(bytes))
                //通过gatt实体类写入值到特征类中
                val result: Boolean? = writeCharacteristic?.setValue(bytes)
                writeDescriptor?.value = bytes
                writeCharacteristic?.addDescriptor(writeDescriptor)
                if (result == true) {
                    mBluetoothGatt.writeCharacteristic(writeCharacteristic)
                }
            } else {
                Toast.makeText(this, "writeCharacteristic为空！", Toast.LENGTH_SHORT).show()
            }

            //TODO 通过Read类型的特征值去发送数据
            readCharacteristic?.value = ByteUtil.toByteArray("0101")
            mBluetoothGatt.writeCharacteristic(readCharacteristic)
        }
    }

    /**
     * 搜索蓝牙设备
     */
    private fun searchBleDevice() {
        mBleAdapter.bluetoothLeScanner.stopScan(mScanCallback)
        //设置扫描参数
        val scanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()
        mBleAdapter.bluetoothLeScanner.startScan(null, scanSettings, mScanCallback)

        Handler(Looper.getMainLooper()).postDelayed({
            mBleAdapter.bluetoothLeScanner.stopScan(mScanCallback)
        }, 5_000)
    }

    /**
     * 链接Gatt服务
     */
    private fun connectGatt(bluetoothDevice: BluetoothDevice) {
        //链接可能存在失败的情况，可以在链接失败的回调里面加上3次重连的逻辑
        Log.d(TAG, "要连接的设备信息:" + bluetoothDevice.name)
        mBluetoothGatt = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            bluetoothDevice.connectGatt(this, true, mGattCallback, BluetoothDevice.TRANSPORT_LE)
        } else {
            bluetoothDevice.connectGatt(this, true, mGattCallback)
        }
    }

    private fun closeBle() {
        mBluetoothGatt.close()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBleService = null
        closeBle()
    }
}