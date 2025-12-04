@echo off
chcp 65001 >nul
echo.
echo ╔══════════════════════════════════════════╗
echo ║         NETTOYAGE SINGLETON DB          ║
echo ║      (Système avec Inscription)         ║
echo ╚══════════════════════════════════════════╝
echo.
echo 📍 Ce script va supprimer :
echo    1. Les fichiers compilés (dossier bin)
echo    2. Les données de la base (dossier database)
echo    3. Les préférences système Java
echo    4. Les données d'inscription utilisateur
echo.

set /p choice=⚠️  Continuer? (O/N): 
if /I "%choice%" NEQ "O" (
    echo ❌ Annulé
    pause
    exit /b 0
)

echo.
echo 🗑️  Suppression en cours...
echo.

REM Supprimer les dossiers locaux
if exist "bin" (
    echo [1/4] Suppression des fichiers compilés...
    rmdir /s /q bin
    echo ✅ Dossier 'bin' supprimé
)

if exist "database" (
    echo [2/4] Suppression des données H2...
    rmdir /s /q database
    echo ✅ Dossier 'database' supprimé
)

if exist "data" (
    echo "   Suppression de l'ancien dossier 'data'..."
    rmdir /s /q data
    echo ✅ Dossier 'data' supprimé
)

echo [3/4] Nettoyage des fichiers .class isolés...
del *.class 2>nul
echo ✅ Fichiers .class supprimés

echo [4/4] Nettoyage des préférences Java...
echo    Cette partie nécessite PowerShell...

REM Nettoyer les préférences Java (Windows)
powershell -Command "
try {
    Write-Host '🔍 Recherche des préférences...'
    
    # Chemin des préférences Java
    \$prefsPath = 'HKCU:\Software\JavaSoft\Prefs'
    
    if (Test-Path \$prefsPath) {
        # Supprimer tous les nœuds commençant par App
        \$nodes = Get-ChildItem \$prefsPath -Recurse | Where-Object {
            \$_.Name -like '*App*' -or \$_.Name -like '*singleton*'
        }
        
        if (\$nodes.Count -gt 0) {
            foreach (\$node in \$nodes) {
                Remove-Item \$node.PsPath -Recurse -Force -ErrorAction SilentlyContinue
                Write-Host \"   ✅ Préférence supprimée: \$(\$node.Name)\"
            }
            Write-Host \"✅ Toutes les préférences ont été supprimées\"
        } else {
            Write-Host \"ℹ️  Aucune préférence trouvée\"
        }
    } else {
        Write-Host \"ℹ️  Aucune préférence Java trouvée\"
    }
    
    # Supprimer aussi le fichier de configuration H2 s'il existe
    if (Test-Path 'singleton_users.mv.db') {
        Remove-Item 'singleton_users.mv.db' -Force
        Write-Host '✅ Fichier H2 supprimé'
    }
    if (Test-Path 'singleton_users.trace.db') {
        Remove-Item 'singleton_users.trace.db' -Force
        Write-Host '✅ Fichier trace H2 supprimé'
    }
    
} catch {
    Write-Host \"❌ Erreur PowerShell: $_\"
}
"

echo.
echo 🔄 Création des dossiers vides...
if not exist "database" mkdir database
if not exist "bin" mkdir bin

echo.
echo 🎉 NETTOYAGE TERMINÉ !
echo.
echo ========================================
echo 📋 Prochaine exécution :
echo ========================================
echo 1. Lancez "run.bat" pour tout recréer
echo 2. Vous verrez l'interface d'inscription
echo 3. Inscrivez-vous avec un nouvel utilisateur
echo 4. Une nouvelle instance Singleton sera créée
echo ========================================
echo.
echo 💡 Pour tester le Singleton :
echo    • Inscrivez-vous une fois
echo    • Redémarrez l'application avec run.bat
echo    • Vérifiez la reconnexion automatique
echo.

pause