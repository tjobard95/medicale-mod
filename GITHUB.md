# Publier le mod sur GitHub (pas à pas)

Ce projet est prêt pour GitHub : il contient un `.gitignore` et une **automatisation**
(`.github/workflows/release.yml`) qui compile le mod et **publie le `.jar` dans une
Release** automatiquement dès que tu crées une version.

Tu as deux méthodes. Choisis-en une.

---

## Méthode A — GitHub Desktop (recommandée si tu débutes, sans ligne de commande)

1. Crée un compte sur https://github.com (si tu n'en as pas).
2. Installe **GitHub Desktop** : https://desktop.github.com
3. Ouvre GitHub Desktop → menu **File → Add Local Repository** → choisis le dossier
   `cottonmod` (celui que tu as décompressé).
   - S'il dit que ce n'est pas un dépôt Git, clique sur **"create a repository"** ici,
     mets le nom `cottonmod`, puis **Create Repository**.
4. Clique sur **Publish repository** (en haut). Choisis **Public**, puis publie.
   → Ton code est maintenant sur GitHub.
5. **Créer une Release téléchargeable** :
   - Va sur ton dépôt sur github.com → onglet **Releases** (à droite) →
     **Draft a new release**.
   - Dans **Choose a tag**, tape `v1.0.0` puis **Create new tag**.
   - Donne un titre (ex: "Cotton Mod 1.0.0") → **Publish release**.
   - Patiente 2-3 min : l'automatisation compile le mod et **attache le fichier
     `cottonmod-1.0.0.jar`** à la Release. Rafraîchis la page, il apparaît dans
     "Assets". C'est ce fichier que les gens téléchargent.

> Pas envie d'attendre l'automatisation ? Compile le `.jar` toi-même
> (dans IntelliJ : Gradle → Tasks → build → build), puis glisse
> `build/libs/cottonmod-1.0.0.jar` dans la zone "Attach binaries" de la Release.

---

## Méthode B — En ligne de commande (Git)

Remplace `TON_PSEUDO` par ton pseudo GitHub. Crée d'abord un dépôt **vide** nommé
`cottonmod` sur github.com (bouton **New**, ne coche PAS "Add a README").

```bash
cd cottonmod

git init
git add .
git commit -m "Initial commit: Cotton Mod"
git branch -M main
git remote add origin https://github.com/TON_PSEUDO/cottonmod.git
git push -u origin main

# Créer la version -> déclenche la compilation + Release automatique :
git tag v1.0.0
git push origin v1.0.0
```

Va ensuite dans l'onglet **Actions** de ton dépôt pour suivre la compilation, puis dans
**Releases** : le `.jar` y sera attaché automatiquement.

---

## Sortir une nouvelle version plus tard

À chaque fois que tu modifies le mod et veux une nouvelle version :

- **GitHub Desktop** : "Commit" tes changements → "Push" → crée une nouvelle Release
  avec un nouveau tag (ex: `v1.1.0`).
- **Ligne de commande** :
  ```bash
  git add . && git commit -m "Nouveautés"
  git push
  git tag v1.1.0 && git push origin v1.1.0
  ```

Le numéro de version du `.jar` reprend automatiquement le tag (`v1.1.0` → `cottonmod-1.1.0.jar`).

---

## Note
La toute première compilation sur GitHub télécharge Minecraft et Fabric : c'est normal
qu'elle prenne quelques minutes. Les suivantes sont plus rapides (mise en cache).
