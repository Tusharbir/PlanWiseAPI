✅ API Documentation

**API Documentation**


### . Full-Text Search

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
      …
    },
    …
  ]
  ```

---

### 4. Autocomplete

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

> **All data** is loaded from **`merged-csv.csv`** in `src/main/resources` at application startup.
> Any additional endpoints (e.g., plan‐detail, filtering) can follow this same pattern: clear URL, description, parameters, example request/response, and notes.
