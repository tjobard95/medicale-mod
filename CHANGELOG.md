# Medical Mod — Patch Notes

**Version actuelle : 1.7.1**
**Minecraft : 1.20.1 · Chargeur : Fabric Loader ≥ 0.15 · Fabric API requis · Java 17**
**Environnement : client + serveur — le mod doit être installé des deux côtés** (rendu 3D, HUD et interfaces côté client ; blessures, physique, potions et butin côté serveur).

---

## Résumé

Medical Mod ajoute une couche « survie/médical » : culture du coton et de plantes médicinales, un **parachute enchantable** porté dans le dos, un **système de blessures** avec soins dédiés, et une **gamme de potions** (dont certaines à effets inédits).

---

## Fonctionnalités

### Parachute

- Porté dans une **case dédiée « dos »** (et non plus dans le plastron) → armure complète **et** parachute simultanément. Clic droit pour l'équiper.
- Affichage **3D sur le dos**, visible par les autres joueurs.
- Durabilité 180, réparable avec du tissu, s'use pendant le vol.
- **Enchantable** (table d'enchantement + livres) et **renommable à l'enclume**.
- Déploiement en maintenant **SHIFT** pendant une chute (ou automatique avec l'enchantement dédié).

**Enchantements**

| Enchantement | Niveaux | Effet | Obtention |
|---|---|---|---|
| Planeur | I–III | Plus de glisse horizontale, descente plus lente | table + livre |
| Atterrissage en douceur | I–II | Annule les dégâts résiduels, divise le risque de fracture | table + livre |
| Toile renforcée | I–III | Usure du parachute divisée | table + livre |
| Déploiement automatique | I | Déploiement sans maintenir SHIFT | **livre uniquement (trésor)** |

- Les livres apparaissent chez les bibliothécaires, à la pêche et dans les coffres.
- « Déploiement automatique » est un enchantement **trésor** : introuvable à la table. Injection garantie (10 %) dans : donjon, épave (réserve), temple de la jungle, bibliothèque de forteresse, cité antique, trésor de cité de l'End.

### Système de blessures

| Blessure | Cause | Effets | Soin |
|---|---|---|---|
| Jambe cassée | Chute ≥ 5 dégâts | Lenteur, **saut impossible** | Attelle |
| Saignement | Coups/flèches ≥ 4 dégâts | Dégâts réguliers, **régénération alimentaire bloquée** | Pansement |
| Infection | Zombies, plaie ouverte | Faiblesse, fatigue, faim accélérée, **régénération alimentaire bloquée** | Médicament |
| Commotion | Explosion, enclume, grosse chute | Nausée, cécité par à-coups | Antidouleur |

- **« Régénération alimentaire bloquée »** : manger ne rend plus de cœurs tant que la blessure n'est pas soignée. Potions, pommes dorées et items de soin fonctionnent toujours.
- Blessures **sauvegardées en NBT**, remises à zéro à la mort.

**Retour d'information au joueur**

- **HUD** en haut à gauche : icône + **« Blessure → Soin »** (ex. *Jambe cassée → Attelle*).
- **Voile coloré** sur les bords, une couleur par blessure (rouge / vert / violet / or).
- **Son distinct** à l'apparition de chaque blessure + rappels sonores discrets pour le saignement et l'infection.
- Message d'alerte nommant le soin à utiliser.

### Items médicaux

Chaque item a un rôle distinct (pas de simple échelle de puissance).

**Soins de blessure** (un par blessure)

| Item | Soigne | Recette |
|---|---|---|
| Attelle | Jambe cassée | bâtons (colonnes) + tissu + fil → 2 |
| Pansement | Saignement | tissu + antiseptique → 2 |
| Médicament | Infection | antiseptique + camomille + seringue → 2 |
| Antidouleur | Commotion | 2 camomille + souci → 2 |

**Consommables de soin**

| Item | Rôle | Recette |
|---|---|---|
| Gel d'aloès | Appoint instantané rapide et bon marché | 1 feuille d'aloès → 2 |
| Pommade | Régénération sur la durée, sans soin instantané | gel d'aloès + souci → 2 |
| Remède à base de plantes | Retire les effets négatifs (antipoison), ne soigne pas les blessures | aloès + camomille + souci + sucre → 2 |
| Kit de soin | Gros soin d'urgence, lent à appliquer | pansement + seringue + remède → 1 |

**Chaîne d'ingrédients** : coton → fil → tissu ; plantes (aloès, camomille, souci) → récoltes ; antiseptique = 2 souci + bouteille en verre ; seringue = vitre + pépite de fer + lingot de fer (sert au médicament et au kit de soin).

### Potions

Cinq potions, chacune en 4 formes (à boire, jet, persistante, flèche).

| Potion | Effet | Durée | Brassage |
|---|---|---|---|
| Célérité | Minage/hache/attaque plus rapides (Célérité II) | 3:00 | embarrassante + éclat d'améthyste |
| Saturation | Remplit la faim d'un coup | instantané | embarrassante + tarte à la citrouille |
| Rayonnement | Contour des mobs (hostiles + pacifiques) dans le noir | 3:00 | embarrassante + sac d'encre luminescente |
| Adrénaline | Force + Rapidité + Célérité, **puis contrecoup** (Fatigue + Lenteur + Faiblesse) | 45 s + 15 s | **potion de Force** + sucre |
| Sérum de premiers secours | **Soigne toutes les blessures** + Régén II + Absorption | instantané | embarrassante + **Kit de soin** |

- Aucune recette n'entre en conflit avec les potions vanilla.
- Le Sérum consomme un Kit de soin → volontairement rare et coûteux, relié au système de blessures.
- Pour distribuer une potion précise (ex. boutique) :
  `/give @p minecraft:potion{Potion:"medical-mod:celerity"}` (remplacer `potion` par `splash_potion`, `lingering_potion` ou `tipped_arrow` selon la forme).

---

## Historique des versions

**1.7.1** — Célérité : recette prismarine → **améthyste** (plus accessible).

**1.7.0 — Potions**
- Ajout des 5 potions ci-dessus (Célérité, Saturation, Rayonnement, Adrénaline, Sérum de premiers secours), 4 formes chacune.
- 3 effets custom (Rayonnement, Adrénaline avec contrecoup, Premiers secours) + recettes de brassage.

**1.6.0 — Retour visuel & sonore par blessure**
- Un son distinct par blessure à l'apparition + rappels sonores discrets (saignement, infection).
- Voile coloré à l'écran propre à chaque blessure.
- Infobulles complètes sur tous les soins.

**1.5.0 — Lisibilité du HUD**
- Le HUD affiche « Blessure → Soin » ; le message d'alerte nomme le soin.

**1.4.0 — Différenciation des soins**
- Rôle unique par consommable (fin de l'échelle de puissance).
- La seringue devient utile (médicament + kit de soin).
- **Correctif** : remède à base de plantes et antidouleur partageaient la même recette (un seul était craftable).

**1.3.0 — Trouvaille des enchantements**
- Déploiement automatique passe en enchantement trésor (livre uniquement) + injection dans 6 types de coffres.
- **Correctif** : suppression d'un mixin qui doublonnait l'ajout des enchantements à la table (probabilités faussées).

**1.2.0 — Parachute repensé & blessures**
- Parachute déplacé du plastron vers une case « dos » dédiée + rendu 3D sur le dos.
- Introduction du système de blessures complet (4 blessures, effets, soins, blocage de la régénération alimentaire).
- **Correctif** : bloc `repositories {}` vide dans `build.gradle` qui empêchait la résolution des dépendances.

**1.1.0 — Base**
- Culture du coton, plantes médicinales, items de soin de base, parachute (version armure).

---

## Notes techniques d'intégration

Points touchant au vanilla, utiles en cas de cohabitation avec d'autres mods :

- **Mixins** : `HungerManager` (blocage régén), `LivingEntity` (blocage du saut jambe cassée), `PlayerEntity` (case dos + blessures + NBT + mort), `PlayerScreenHandler` et `InventoryScreen` (case dos dans l'inventaire).
- **Tables de butin** : injection de livres d'enchantement via `LootTableEvents.MODIFY` sur 6 tables vanilla listées plus haut (n'écrase rien, ajoute une pool à 10 %).
- **Effets & potions** : 3 `StatusEffect` custom et 5 `Potion` enregistrés ; brassage via l'API Fabric.
- **Réseau** : paquet serveur → client synchronisant l'état des blessures et le parachute porté (nécessaire au HUD et au rendu du dos).
- **Groupe créatif** : onglet dédié « Medical Mod » regroupant tous les items.

---

## Statut & point d'attention pour le build

- Code complet et cohérent, prêt à compiler (Gradle / Fabric Loom).
- **À valider au build** : l'enregistrement du brassage utilise `FabricBrewingRecipeRegistry`, dont la signature a évolué selon les versions de Fabric API. À confirmer avec la version exacte utilisée sur le serveur (fabric_version pour 1.20.1 : 0.92.x).
- Le mixage sonore et l'intensité des voiles à l'écran se règlent en jeu selon le ressenti (valeurs facilement ajustables).

## 1.7.2 — Correctif de compilation

- **Correctif** : `LootTableEvents.MODIFY` (injection des livres d'enchantement dans les coffres)
  utilisait une signature a 3 parametres au lieu des 5 attendus par Fabric API 1.20.1
  (`resourceManager, lootManager, id, tableBuilder, source`). Corrige dans `LootIntegration.java`.
  C'etait le point signale comme "a verifier au build" — confirme et resolu.

## 1.8.0 — Saturation : chaine de production longue

- La Potion de Saturation ne se brasse plus en une etape. Nouvelle chaine en 3 etapes :
  1. Potion embarrassante + tranche de melon scintillant -> Base nutritive
  2. Base nutritive + carotte doree -> Base enrichie
  3. Base enrichie + Concentre nutritif -> Potion de Saturation
- Nouvel item **Concentre nutritif** : craft couteux (4 tartes a la citrouille, 2 fioles de miel,
  2 carottes dorees, 1 pomme doree) -> 2 concentres.
- Objectif : rendre la Saturation chere a produire pour qu'elle ait de la valeur en boutique.

## 1.9.0 — Distillateur nutritif (machine exclusive)

- Nouveau bloc **Distillateur nutritif** : **aucune recette de craft**. Il ne s'obtient
  que via `/give` (niveau OP) ou l'inventaire creatif — c'est ce qui garantit l'exclusivite.
- Le bloc est **incassable** (durete -1, comme la bedrock) : personne ne peut le detruire
  ni le voler. Seul un joueur en creatif peut le retirer.
- **La Potion de Saturation n'a plus de recette de brassage.** Elle se fabrique
  uniquement dans le Distillateur : bouteille d'eau + Concentre nutritif -> 10 s -> Saturation.
- Le **Concentre nutritif** reste craftable par tous : les joueurs peuvent en produire
  et te les vendre, mais seul le proprietaire de la machine peut les transformer en potion.
- Interface avec 2 entrees, 1 sortie et une barre de progression.

Commande pour l'obtenir : `/give @p medical-mod:potion_machine`

## 2.0.0 — Medical Mod (renommage) + potions exclusives

**Le mod s'appelle desormais Medical Mod.**
- Identifiant technique : `medicalmod` · Jar : `medical-mod-2.0.0.jar`
- Toutes les commandes changent : `medicalmod:` au lieu de `cottonmod:`

**Potions : 3 au total, toutes exclusives a la machine**
- **Supprimee** : Potion de Celerite. Son effet (vitesse de minage) reste present,
  integre a l'Adrenaline (Celerite II).
- **Supprimee** : Potion de Rayonnement (et son effet custom).
- **Plus aucune recette d'alambic.** Les 3 potions restantes se fabriquent
  UNIQUEMENT dans le Distillateur nutritif :
  - bouteille d'eau + tarte a la citrouille -> Saturation
  - bouteille d'eau + seringue -> Adrenaline
  - bouteille d'eau + kit de soin -> Serum de premiers secours
- Recette de la Saturation simplifiee (le cout n'a plus a etre eleve : l'exclusivite
  vient de la machine, pas de l'ingredient).
- **Supprime** : item Concentre nutritif, devenu inutile.

**Rappel** : le Distillateur n'a aucune recette de craft et est incassable.
`/give @p medicalmod:potion_machine`

## 2.0.1 — Correctif de compilation

- **Correctif** : `PotionMachineBlock.getTicker()` appelait `validateTicker(...)`, une methode
  qui n'existe que sur `BlockWithEntity` — or ce bloc etend `Block` directement et implemente
  `BlockEntityProvider` a part. Remplace par une comparaison de type manuelle. Le Distillateur
  nutritif compile et fonctionne desormais correctement.

## 2.0.2 — Correctif de crash au demarrage (critique)

- **Correctif** : le Distillateur nutritif utilisait `FabricBlockSettings.copyOf(Blocks.BLAST_FURNACE)`.
  Cette copie recupere aussi la **fonction de luminosite** du haut-fourneau, qui lit la propriete
  d'etat `lit`. Or le Distillateur ne possede pas cette propriete : Minecraft levait une
  `IllegalArgumentException` et le jeu refusait de demarrer.
- Remplace par des reglages explicites (`FabricBlockSettings.create()`) avec une luminosite
  **constante**, independante de tout blockstate.
- Les blocs de culture (coton, aloes, camomille, souci) copient `Blocks.WHEAT`, qui n'a
  aucune luminosite dependant d'un etat : ils n'etaient pas concernes.

Cette version corrige un crash bloquant : la 2.0.0 et la 2.0.1 sont inutilisables.

## 2.1.0 — Interface du Distillateur nutritif

- **Texture d'interface sur mesure** (`textures/gui/potion_machine.png`).
  Avant, la machine reutilisait la texture du coffre vanilla : 27 cases affichees
  alors que la machine n'en a que 3, ce qui rendait l'interface illisible.
- Nouvelle disposition claire :
  - 2 cases d'entree empilees a gauche (bouteille d'eau en haut, ingredient en bas)
  - fleche de progression au centre, qui se remplit pendant les 10 secondes de fabrication
  - case de sortie a droite, encadree pour la distinguer
- La barre de progression dessinee "a la main" est remplacee par une vraie fleche texturee.

## 2.1.1 — Potions visibles dans l'onglet creatif

- **Correctif** : les 3 potions du mod n'apparaissaient pas dans l'onglet creatif "Medical Mod".
  Une potion n'est pas un Item a part entiere : c'est un `minecraft:potion` auquel on applique
  une potion via NBT. Elle doit donc etre ajoutee explicitement au groupe, ce qui n'etait pas fait.
- Saturation, Adrenaline et Serum de premiers secours apparaissent desormais dans l'onglet,
  sous leurs 3 formes : a boire, en jet, persistante.
- Ordre d'enregistrement corrige : les potions sont enregistrees AVANT le groupe creatif.

## 2.2.0 — Blessures beaucoup plus rares et entierement reglables

Le systeme etait trop punitif : une chute de 10 degats donnait 70% de fracture,
et un combat de 5 coups 71% de saignement. Rééquilibrage complet.

**Nouvelles probabilites (par defaut)**
| Situation | Avant | Apres |
|---|---|---|
| Chute 10 degats -> fracture | 70% | 8% |
| Chute 14 degats -> fracture | 100% | 24% |
| Chute 25 degats -> fracture | 100% | 60% (plafond) |
| Saignement, 5 coups recus | 71% | 30% |
| Infection | 12% | 4% |
| Commotion (explosion) | 40% | 10% |

- Seuil de fracture releve de 5 a 8 degats de chute : les petites chutes ne cassent plus rien.
- Seuil de saignement releve de 4 a 6 degats.
- **Plafond** de 60% sur la fracture : meme une chute enorme n'est jamais une certitude.
- **Max 2 blessures simultanees** : impossible de cumuler les 4.
- **Repit de 30 s** apres une blessure : plus d'enchainement en rafale.

**Tout est desormais reglable dans `config/medicalmod.json`**, sans recompiler.

**Nouvelles commandes**
| Commande | Niveau | Effet |
|---|---|---|
| `/medicalmod info` | tous | Affiche les reglages actuels |
| `/medicalmod difficulty <0.0-3.0>` | OP 2 | Multiplie toutes les probabilites |
| `/medicalmod injuries on\|off` | OP 2 | Active / coupe les blessures |
| `/medicalmod heal` | OP 2 | Soigne toutes ses blessures |
| `/medicalmod reload` | OP 2 | Recharge la config |
