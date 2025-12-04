@echo off
chcp 65001 >nul
echo.
echo ╔══════════════════════════════════════════╗
echo ║      SINGLETON DB - AVEC INSCRIPTION    ║
echo ╚══════════════════════════════════════════╝
echo.
echo 📍 Emplacement : %CD%
echo.

REM Vérifier que nous sommes au bon endroit
if not exist "src\App.java" (
    echo ❌ ERREUR : Fichier App.java introuvable !
    echo.
    echo Structure attendue :
    echo 📁 VotreDossier/
    echo   ├── 📄 run.bat
    echo   ├── 📄 clean.bat
    echo   ├── 📁 src/
    echo   │    └── App.java
    echo   └── 📁 lib/
    echo        └── h2.jar
    echo.
    pause
    exit /b 1
)

REM Créer les dossiers nécessaires
echo 📁 Préparation des dossiers...
if not exist "lib" mkdir lib
if not exist "database" mkdir database
if not exist "bin" mkdir bin

echo ✅ Dossiers prêts
echo.

REM Vérifier Java
echo 🔍 Vérification de Java...
where java >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java n'est pas installé ou n'est pas dans le PATH
    echo.
    echo 📥 Solutions :
    echo 1. Téléchargez Java : https://adoptium.net/
    echo 2. Ajoutez Java au PATH
    echo.
    pause
    exit /b 1
)

java -version 2>&1 | findstr "version"
echo ✅ Java détecté
echo.

REM Vérifier/télécharger H2
echo 🔍 Vérification du driver H2...
if not exist "lib\h2.jar" (
    echo ❌ Driver H2 introuvable
    echo 📥 Téléchargement automatique...
    echo.
    
    powershell -Command "
    try {
        Write-Host 'Téléchargement depuis Maven Central...'
        \$url = 'https://repo1.maven.org/maven2/com/h2database/h2/2.2.224/h2-2.2.224.jar'
        Invoke-WebRequest -Uri \$url -OutFile 'lib\h2.jar'
        
        if (Test-Path 'lib\h2.jar') {
            \$size = (Get-Item 'lib\h2.jar').Length / 1MB
            Write-Host \"✅ Téléchargement réussi !\"
            Write-Host \"   Taille : {0:F2} MB\" -f \$size
        } else {
            Write-Host '❌ Échec du téléchargement'
        }
    } catch {
        Write-Host \"❌ Erreur : $_\"
    }
    "
    
    if not exist "lib\h2.jar" (
        echo.
        echo ⚠️  Téléchargement échoué
        echo 📥 Méthode manuelle :
        echo 1. https://repo1.maven.org/maven2/com/h2database/h2/
        echo 2. Téléchargez h2-2.2.224.jar
        echo 3. Placez dans 'lib\'
        echo 4. Renommez en 'h2.jar'
        echo.
        pause
        
        if not exist "lib\h2.jar" (
            echo ❌ Driver introuvable
            pause
            exit /b 1
        )
    )
)

echo ✅ Driver H2 prêt
echo.

REM Compilation
echo 🔨 Compilation du code...
javac -cp "lib\h2.jar" -d bin src\App.java 2> compilation_errors.txt

if %errorlevel% neq 0 (
    echo ❌ Erreur de compilation !
    echo.
    type compilation_errors.txt
    echo.
    echo 🔧 Vérifiez :
    echo 1. Que le code est complet
    echo 2. Les guillemets et parenthèses
    echo 3. Pas d'erreurs de syntaxe
    echo.
    del compilation_errors.txt
    pause
    exit /b 1
)

del compilation_errors.txt 2>nul
echo ✅ Compilation réussie
echo.

REM Lancement
echo 🚀 Lancement de l'application...
echo.
echo ========================================
echo      SINGLETON DB - AVEC INSCRIPTION
echo ========================================
echo Fonctionnalités :
echo • 📝 Inscription utilisateur
echo • 🔄 Connexion automatique au redémarrage
echo • 🎯 Une seule instance Singleton
echo • 🧪 Test intégré du pattern
echo • 🗑️  Suppression de compte
echo ========================================
echo.

echo 📢 Messages de la console :
echo ===========================
java -cp "bin;lib\h2.jar" App

echo.
echo ===========================
echo L'application s'est fermée
echo.
echo 💡 Pour nettoyer tout et recommencer :
echo    Exécutez "clean.bat"
echo.
pause