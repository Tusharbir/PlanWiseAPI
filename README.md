✅ API Documentation

**API Documentation**


### 1. Full-Text Search

* **URL**: `[GET /api/search?q={query}](http://localhost:8080/api/search?q=50)`
* **Description**:
  Performs a full-text search across *all* plan fields.
* **Query Parameter**:

  * `q` *(required)* – search term (letters or digits).
* **Behavior**:

  1. If `q` is null/blank or contains no alphanumeric characters → returns `[]`.
  2. If `q` exactly matches a site key → returns same result as **Get Plans by Site**.
  3. Otherwise → returns all plans whose concatenated “rawText” (every field) contains `q` (case-insensitive).
* **Example**:

  ```
  GET http://localhost:8080/api/search?q=stream
  ```
* **Response** (200 OK):

  ```json
  [
    {
      "site": "vmedia",
      "provider": "",
      "planName": "Internet 30 Unlimited",
      "features": "… video streaming and video-calling …",
      "description": "… video streaming …",
  
    },
  
  ]
  ```

---

### 2. Autocomplete

* **URL**: ` http://localhost:8080/api/autocomplete?prefix={prefix}&max={maxCount}`
* **Description**:
  Suggests up to `maxCount` tokens (words/numbers) from the entire CSV that start with the given prefix, powered by an AVL-tree autocomplete engine.
* **Query Parameters**:

  * `prefix` *(required)* – partial input (must include at least one letter or digit).
  * `max` *(optional)* – maximum number of suggestions (default: `5`).
* **Behavior**:

  * Returns `[]` if `prefix` is null/blank or symbol-only.
  * Otherwise, returns the top `max` tokens whose term starts with `prefix`.
* **Example**:

  ```
  GET http://localhost:8080/api/autocomplete?prefix=ro&max=10
  ```
* **Response** (200 OK):

  ```json
  [
    "rogers",
    "router",
    "room",
    "roaming",
    "300"
  ]
  ```

---

### 3. Spell-Check Suggestions

**URL**  
`GET /api/spellcheck?word={word}&max={maxCount}`

**Description**  
Returns up to `maxCount` suggested corrections for the input `word`, based on a Levenshtein-distance search over the entire CSV vocabulary loaded into a Trie.

**Query Parameters**  
- `word` _(required)_: term to check (e.g. `strem`).  
- `max` _(optional)_: maximum number of suggestions to return (default `5`).

**Behavior**  
1. If `word` is null or blank → returns `[]`.  
2. Otherwise, finds all tokens within edit-distance ≤ 2, sorts them by (distance ascending, then alphabetically), and returns the top `max`.

**Example Request**  
```http
GET http://localhost:8080/api/spellcheck?word=strem&max=5
```
**Example Response**
```
[
  "stream",
  "strem",
  "streem",
  "stress",
  "steam"
]
```
Notes
	•	Vocabulary is extracted from all columns of merged-csv.csv at application startup.
	•	Suggestions are case-insensitive and returned in lowercase.


> **All data** is loaded from **`merged-csv.csv`** in `src/main/resources` at application startup.

