# Medical Mod (Fabric 1.20.1)

Mod de survie : coton, plantes medicinales, systeme de blessures, et
parachute enchantable porte dans le dos.

## Telecharger le mod compile (le plus simple)

Une release GitHub avec le `.jar` pret a l'emploi est disponible ici :
**https://github.com/tjobard95/medical-mod/releases**

Prends le fichier `medical-mod-x.x.x.jar` de la derniere release, pas besoin
de compiler quoi que ce soit.

Si aucune release n'existe encore : va dans l'onglet **Actions** du depot,
ouvre le dernier build reussi (coche verte), et telecharge l'artifact
`medical-mod-jar` tout en bas de la page.

## Installation (cote client ET serveur)

1. Installer **Fabric Loader** pour Minecraft 1.20.1 : https://fabricmc.net/use/installer
2. Telecharger **Fabric API** (meme version 1.20.1) : https://modrinth.com/mod/fabric-api
3. Placer les deux fichiers `.jar` (Fabric API + Medical Mod) dans le dossier `mods/` :
   - Windows : `%appdata%\.minecraft\mods`
   - Serveur : dossier `mods/` a la racine du serveur Fabric
4. Java 17 minimum requis.

Le mod doit etre present **des deux cotes** (client et serveur) : il gere
du rendu (parachute, HUD) et de la logique serveur (blessures, potions).

## Contenu

**Coton**
- Graines -> buisson de coton (8 stades). Recolte a maturite.
- Coton -> Fil -> Tissu -> base de plusieurs objets de soin et du parachute.

**Plantes medicinales**
- Aloes -> Gel d'aloes (soin rapide)
- Camomille -> entre dans plusieurs recettes (antidouleur, remede...)
- Souci -> Antiseptique

**Parachute**
- Se porte dans une case dediee "dos" (clic droit), en plus de l'armure.
- Enchantable a la table et via des livres trouvables.
- Enchantements : Planeur, Atterrissage en douceur, Toile renforcee,
  Deploiement automatique (tresor, livre uniquement).

**Systeme de blessures**
- Jambe cassee (Attelle), Saignement (Pansement), Infection (Medicament),
  Commotion (Antidouleur). Manger ne soigne plus tant que la blessure est active.

**Potions**
- Celerite, Saturation, Rayonnement, Adrenaline, Serum de premiers secours.

Le detail complet de chaque fonctionnalite et l'historique des versions
sont dans `CHANGELOG.md`.

## Compiler soi-meme (pour developper / modifier le mod)

1. Installer JDK 17 (adoptium.net).
2. Dans le dossier du projet :
   - Windows : `gradlew.bat build`
   - Mac/Linux : `./gradlew build`
3. Le jar compile se trouve dans `build/libs/medical-mod-x.x.x.jar`.
4. Pour tester en jeu directement : `./gradlew runClient`

## Publier une nouvelle version (pour le mainteneur du depot)

Le depot compile automatiquement a chaque `push` (voir l'onglet Actions).
Pour generer une **release telechargeable** avec le jar attache :

```bash
git tag v1.7.1
git push origin v1.7.1
```

Une Release GitHub est creee automatiquement avec le `.jar` en piece jointe.
