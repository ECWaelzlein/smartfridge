# Smart Fridge
## Food API
Es wird folgende Api verwendet, um mit Hilfe eines Barcodes ein Lebensmittel zu erkennen: https://world.openfoodfacts.org/api/v0/product/barcode.json.

## Endpunkte nach außen
In dem Header jedes Aufrufs befindet sich ein JSON Web Token, der für die Authentifizierung zuständig ist.
Der Request Body besteht aus einem JSON String.

| Service       | Endpunkt                          |Request Method| Request Body | Description
| ------------- |:----------------------------------|:------------:|:-------------|:---------------------------------------------------------|
| Dispatcher    |/api/dispatcher/food                                   | POST         |barcode| Erstellt ein Lebensmittel.                               |
| Dispatcher    |/api/dispatcher/food/*id*                              | POST         |Anzahl, Datum| Ändert ein Lebensmittel.                               |
| Dispatcher    |/api/dispatcher/food/*id*                              | GET          |/| Fragt ein Lebensmittel an.                               |
| Dispatcher    |/api/dispatcher/recipe                                 | POST         |Liste an Zutaten| Gibt ein Rezept basierend auf Zutaten zurück.            |
| Dispatcher    |/api/dispatcher/recipe?random=True&tags=veggie,dessert | GET          |/| Gibt ein zufälliges Rezept zurück.                       |
| Dispatcher    |/api/dispatcher/meal                                   | POST         |Datum, Gerichtname| Erstellen eines geplanten Gerichts.                      |
| Dispatcher    |/api/dispatcher/meal/*id*                              | POST         |Datum, Gerichtname| Ändern eines geplanten Gerichts.                         |
| Dispatcher    |/api/dispatcher/meal/*id*                              | DELETE       |/| Löschen eines geplanten Gerichts.                        |
| Dispatcher    |/api/dispatcher/meal/:*id*                             | GET          |/| Bekommen eines geplanten Gerichts oder alle Gerichte.    |
| Dispatcher    |/api/dispatcher/shopping-list                          | GET          |/| Bekommen der Einkaufsliste der nächsten 7 Tage.                              |

## Endpunkte intern

| Service       | Endpunkt                          |Request Method| Request Body | Description
| ------------- |:----------------------------------|:------------:|:-------------|:---------------------------------------------------------|
| FoodController             |/api/food        | POST         |barcode| Erstellt ein Lebensmittel.                               |
| FoodController             |/api/food/*id*   | GET          |/| Fragt ein Lebensmittel an.                               |
| FoodController             |/api/food/*id*   | DELETE       |/| Löscht ein Lebensmittel.                                 |
| FoodInventoryController    |/api/food-inventory/   | POST       ||                                  |
