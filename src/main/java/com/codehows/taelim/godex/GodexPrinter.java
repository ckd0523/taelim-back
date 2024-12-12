package com.codehows.taelim.godex;

import org.springframework.stereotype.Component;


@Component
public class GodexPrinter
{
	EZioLib.API API = EZioLib.API.INSTANCE;
	public clsPrinterConfig Config = new clsPrinterConfig();
	public clsPrinterCommand Command = new clsPrinterCommand();
	
	//---------------------------------------------------------------------------
	// Open By Communication Type
	//---------------------------------------------------------------------------
	public void Open(int nPortType)
	{
		API.openport(Integer.toString(nPortType));
	}

	//---------------------------------------------------------------------------
	// Open By String
	//---------------------------------------------------------------------------
	public void Open(String PortName)
	{
		if (PortName.contains("COM") == true)
	    	API.OpenUSB(PortName);
	    else
	    	API.OpenDriver(PortName);
	}

	//---------------------------------------------------------------------------
	// Open By Network
	//---------------------------------------------------------------------------
	public void Open(String strIP, String strPort)
	{
		API.OpenNet(strIP, strPort);
	}

	//---------------------------------------------------------------------------
	// Set COM Port (RS232) Baudrate
	//---------------------------------------------------------------------------
	public void SetBaudrate(int nBaud)
	{
		API.setbaudrate(nBaud);
	}

	//---------------------------------------------------------------------------
	// Close
	//---------------------------------------------------------------------------
	public void Close()
	{
		API.closeport();
	}

	//---------------------------------------------------------------------------
	// Get Dll Version
	//---------------------------------------------------------------------------
	public String GetVersion()
	{	
		byte[] ByteData = new byte[50];
		API.GetDllVersion(ByteData);
		String strData = new String(ByteData);
	    return strData;
	}

	//---------------------------------------------------------------------------
	
	
}
