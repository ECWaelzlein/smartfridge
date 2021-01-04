# Smart Fridge
## Dokumentation
Die Dokumentation ist zu finden unter: https://www.overleaf.com/6247266947qxvpqtqspqcc
## Food API
Es wird folgende Api verwendet, um mit Hilfe eines Barcodes ein Lebensmittel zu erkennen: https://world.openfoodfacts.org/api/v0/product/barcode.json.
## Recipe API
Es wird folgende Api verwendet, um Rezepte (z.B. basierend auf den Zutaten etc.) zu erhalten: https://api.spoonacular.com/recipes/.

## Endpunkte nach außen
In dem Header jedes Aufrufs befindet sich ein JSON Web Token, der für die Authentifizierung zuständig ist.
Der Request Body besteht aus einem JSON String.

| Service       | Endpunkt                          |Request Method| Request Body | Response Body | Description
| ------------- |:----------------------------------|:------------:|:-------------|:------|:---------------------------------------------------------|
| Dispatcher    |/api/dispatcher/food                                   | POST         |barcode|food| Erstellt ein Lebensmittel.                               |
| Dispatcher    |/api/dispatcher/food/*id*                              | POST         |amount, date|food, amount| Ändert ein Lebensmittel.                               |
| Dispatcher    |/api/dispatcher/food/*id*                              | GET          |/|food| Fragt ein Lebensmittel an.                               |
| Dispatcher    |/api/dispatcher/recipe                                 | POST         |[ingredients]|recipe| Gibt ein Rezept basierend auf Zutaten zurück.            |
| Dispatcher    |/api/dispatcher/recipe?random=True&tags=veggie,dessert | POST         |/|recipe| Gibt ein zufälliges Rezept zurück.                       |
| Dispatcher    |/api/dispatcher/meal                                   | POST         |date, mealName|meal| Erstellen eines geplanten Gerichts.                      |
| Dispatcher    |/api/dispatcher/meal/*id*                              | POST         |date, mealName|meal| Ändern eines geplanten Gerichts.                         |
| Dispatcher    |/api/dispatcher/meal/*id*                              | DELETE       |/|/ (HTTP Status Code: 204)| Löschen eines geplanten Gerichts.                        |
| Dispatcher    |/api/dispatcher/meal/:*id*                             | GET          |/|meal oder [meal]| Bekommen eines geplanten Gerichts oder alle Gerichte.    |
| Dispatcher    |/api/dispatcher/shopping-list                          | GET          |/|"ingredients":[ { "ingredient": "apple", "quantity": "1" } ]| Bekommen der Einkaufsliste der nächsten 7 Tage.                              |

## Endpunkte intern

| Service                    | Endpunkt                                       |Request Method| Request Body | Response Body | Description
| ---------------------------|:-----------------------------------------------|:------------:|:-------------|:--------------|:---------------------------------------------------------|
| FoodController             |/api/food                                       | POST         |barcode|food| Erstellt ein Lebensmittel.                               |
| FoodController             |/api/food/*id*                                  | GET          |/|food| Fragt ein Lebensmittel an.                               |
| FoodController             |/api/food/*id*                                  | DELETE       |/|/ (HTTP Status Code: 204)| Löscht ein Lebensmittel.                                 |
| FoodInventoryController    |/api/food-inventory/                            | POST         |food, amount, expDate| id      |   Legt ein Food-Objekt in das FoodInventory.    |
| FoodInventoryController    |/api/food-inventory/:*id*                       | GET          |/| foodInventory oder   [foodInventory]   |   Bekommt ein FoodInventory-Objekt oder eine Liste von allen Objekten.    |
| FoodInventoryController    |/api/food-inventory/*id*                        | DELETE       |/|/ (HTTP Status Code: 204)    |   Löscht ein Food-Objekt aus dem FoodInventory.    |
| RecipeController           |/api/recipe/                                    | POST         |[ingredients] |recipe    |   Erstellt ein Rezept.    |
| RecipeController           |/api/recipe/random?tags=veggie,dessert     | POST         |/|recipe    |   Erstellt ein zufälliges Rezept.    |
| RecipeController           |/api/recipe/*id*                                | GET          |/|recipe    |   Bekommt ein Rezept zu einer ID.    |
| MealController             |/api/meal/                                      | POST         |date, mealName |meal    |   Erstellt eine Mahlzeit.    |
| MealController             |/api/meal/:*id*                                 | GET          |/|meal oder [meal]    |   Bekommt eine Mahlzeit oder eine Liste an Mahlzeiten.    |
| MealController             |/api/meal/*id*                                  | DELETE       |/|/ (HTTP Status Code: 204)    |   Löscht eine Mahlzeit.    |
| MealController             |/api/meal/*id*                                  | POST         |date|meal    |   Ändert eine Mahlzeit.    |
