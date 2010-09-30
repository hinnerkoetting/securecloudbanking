::legacy
SET FIREFOX="C:\Program Files (x86)\Mozilla Firefox\firefox.exe"
SET EXTENSIONSFOLDER=C:\Users\Hinni\AppData\Roaming\Mozilla\Firefox\Profiles\nf2jybuf.default\extensions\
SET GREASEMONKEY_DIR=.\
SET GREASEMONKEY_NAME={e4a8a97b-f2ed-450b-b12d-ee082ba24781}
SET PROFILE_DIRECTORY=C:\Users\Hinni\AppData\Roaming\Mozilla\Firefox\Profiles\nf2jybuf.default
SET GM_HACK_DIR=.\gm_scripts

::copy greasemonkey files to firefox extension folder
mkdir %EXTENSIONSFOLDER%\%GREASEMONKEY_NAME%
xcopy /E /Y %GREASEMONKEY_DIR%%GREASEMONKEY_NAME% %EXTENSIONSFOLDER%\%GREASEMONKEY_NAME%
::copy hack gm_file to profile directory
mkdir %PROFILE_DIRECTORY%\%GM_HACK_DIR%
xcopy /E /Y %GM_HACK_DIR% %PROFILE_DIRECTORY%\%GM_HACK_DIR%
::start firefox (it will display window "new add-on installed"
START "" %FIREFOX%
::wait some seconds so that firefox will be started
ping 127.0.0.1 -n 5 -w 1000 > NUL
::kill addon windows
TASKKILL /FI "WINDOWTITLE eq Add-ons"
::kill firefox window
TASKKILL /IM firefox.exe


