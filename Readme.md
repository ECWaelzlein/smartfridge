# Smart Fridge
## Dokumentation
Die Dokumentation ist zu finden unter: https://www.overleaf.com/6247266947qxvpqtqspqcc
## Food API
Es wird folgende Api verwendet, um mit Hilfe eines Barcodes ein Lebensmittel zu erkennen: https://world.openfoodfacts.org/api/v0/product/barcode.json.
## Recipe API
Es wird folgende Api verwendet, um Rezepte (z.B. basierend auf den Zutaten etc.) zu erhalten: https://api.spoonacular.com/recipes/.

## Endpunkte

| Service                    | Endpunkt                                       |Request Method| Request Body | Response Body | Description
| ---------------------------|:-----------------------------------------------|:------------:|:-------------|:--------------|:---------------------------------------------------------|
| FoodController             |/api/food                                       | POST         |barcode|food| Erstellt ein Lebensmittel.                               |
| FoodController             |/api/food/*id*                                  | GET          |/|food| Fragt ein Lebensmittel an.                               |
| FoodController             |/api/food/*id*                                  | DELETE       |/|/ (HTTP Status Code: 204)| Löscht ein Lebensmittel.                                 |
| FoodInventoryController    |/api/food-inventory                             | POST         |foodId, userId, expDate| foodInventory      |   Legt ein Food-Objekt in das FoodInventory.    |
| FoodInventoryController    |/api/food-inventory/expire?days=*3*             | GET          |/| [foodInventory]      |   Bekommt eine Liste aller in x Tagen ablaufenden FoodInventories    |
| FoodInventoryController    |/api/food-inventory/:*id*                       | GET          |/| foodInventory oder   [foodInventory]   |   Bekommt ein FoodInventory-Objekt oder eine Liste von allen Objekten.    |
| FoodInventoryController    |/api/food-inventory/*id*                        | DELETE       |/|/ (HTTP Status Code: 204)    |   Löscht ein Food-Objekt aus dem FoodInventory.    |
| RecipeController           |/api/recipe                                     | POST         |[ingredients] |recipe    |   Erstellt ein Rezept.    |
| RecipeController           |/api/recipe/random?tags=*veggie,dessert*        | POST         |/|recipe    |   Erstellt ein zufälliges Rezept.    |
| RecipeController           |/api/recipe/*id*                                | GET          |/|recipe    |   Bekommt ein Rezept zu einer ID.    |
| MealController             |/api/meal                                       | POST         |date, userId, recipeId |meal    |   Erstellt eine Mahlzeit.    |
| MealController             |/api/meal/:*id*                                 | GET          |/|meal oder [meal]    |   Bekommt eine Mahlzeit oder eine Liste an Mahlzeiten.    |
| MealController             |/api/meal/*id*                                  | DELETE       |/|/ (HTTP Status Code: 204)    |   Löscht eine Mahlzeit.    |
| MealController             |/api/meal/*id*                                  | POST         |date|meal    |   Ändert eine Mahlzeit.    |
| AccountController          |/api/account                                    | POST         |username,password|account    |   Erstellt einen Account.    |
| AccountController          |/api/account/*id*                               | GET          |/|account   |   Erhält einen Account.    |
