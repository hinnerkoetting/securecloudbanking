FIREFOX ="""C:\Program Files (x86)\Mozilla Firefox\firefox.exe"""
EXTENSIONSFOLDER ="C:\Users\Hinni\AppData\Roaming\Mozilla\Firefox\Profiles\nf2jybuf.default\extensions\"
PROFILE_DIRECTORY ="C:\Users\Hinni\AppData\Roaming\Mozilla\Firefox\Profiles\nf2jybuf.default" 
GREASEMONKEY_NAME="{e4a8a97b-f2ed-450b-b12d-ee082ba24781}"
GM_HACK_DIR=".\gm_scripts"

Set WshShell = WScript.CreateObject("WScript.Shell")
'copy greasemonkey files to firefox extension folder

'first create folder
Set fso = CreateObject("Scripting.FileSystemObject")
targetDir = EXTENSIONSFOLDER & GREASEMONKEY_NAME
if not fso.FolderExists(targetDir) then 
	fso.CreateFolder(targetDir)
end if

'copy
fso.CopyFolder GREASEMONKEY_NAME, targetDir


'copy hack gm_file to profile directory
'create first again
targetDir = PROFILE_DIRECTORY & GM_HACK_DIR
if not fso.FolderExists(targetDir) then 
	fso.CreateFolder(targetDir)
end if

'copy
fso.CopyFolder  GM_HACK_DIR, targetDir

'start firefox (it will display window "new add-on installed"
Set WshShell= WScript.CreateObject("WScript.Shell")
WScript.Sleep 2000
WshShell.Run FIREFOX
'wait some seconds so that firefox will be started
WScript.Sleep 5000
'kill addon windows
WshShell.Run "TASKKILL /FI ""WINDOWTITLE eq Add-ons"""
'kill firefox window
WshShell.Run "TASKKILL /IM firefox.exe"


