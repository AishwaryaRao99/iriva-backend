# Product API - Pagination, Search & Sorting Guide

This document provides a comprehensive guide to the enhanced Product API with pagination, sorting, and advanced filtering capabilities.

## Overview

The Product API now supports:
- **Pagination**: Retrieve products in pages with customizable page size
- **Sorting**: Sort results by multiple fields in ascending or descending order
- **Filtering**: Filter products by various criteria (scores, brand, category)
- **Advanced Search**: Complex multi-criteria filtering

---

## Response Format

All paginated endpoints return a `PageResponse` object with the following structure:

```json
{
  "content": [
    {
      "id": 1,
      "productName": "Example Product",
      "description": "Product Description",
      "brand": "Brand Name",
      "category": "FOOD",
      "ethicalScore": 85.5,
      "transparencyScore": 90.0,
      "imageUrl": "url/to/image",
      "ethicalSummary": [...],
      "ingredients": [...],
      "transparencyAnalysis": {...}
    }
  ],
  "totalElements": 150,
  "totalPages": 15,
  "currentPage": 0,
  "pageSize": 10,
  "hasNext": true,
  "hasPrevious": false,
  "sortedBy": "productName",
  "sortDirection": "ASC"
}
```

---

## API Endpoints

### 1. Basic Pagination Endpoints

#### Get All Products (Paginated)
**Endpoint:** `GET /api/v1/productsapi/paginated/all`

**Query Parameters:**
- `page` (optional, default: 0) - Page number (0-indexed)
- `size` (optional, default: 10) - Number of products per page (1-100)
- `sortBy` (optional, default: "productName") - Field to sort by
  - Available values: `productName`, `ethicalScore`, `transparencyScore`, `brand`
- `sortDirection` (optional, default: "ASC") - Sort direction: `ASC` or `DESC`

**Examples:**
```bash
# Get all products, page 0, 10 items per page, sorted by product name ascending
GET http://localhost:8080/api/v1/productsapi/paginated/all

# Get page 2 with 20 items per page, sorted by ethical score descending
GET http://localhost:8080/api/v1/productsapi/paginated/all?page=2&size=20&sortBy=ethicalScore&sortDirection=DESC

# Get all products sorted by brand ascending
GET http://localhost:8080/api/v1/productsapi/paginated/all?sortBy=brand&sortDirection=ASC
```

---

#### Get Products by Category (Paginated)
**Endpoint:** `GET /api/v1/productsapi/paginated/by-category`

**Query Parameters:**
- `category` (required) - Product category (e.g., FOOD, ELECTRONICS, CLOTHING)
- `page` (optional, default: 0) - Page number
- `size` (optional, default: 10) - Number of products per page
- `sortBy` (optional, default: "productName") - Sort field
- `sortDirection` (optional, default: "ASC") - Sort direction

**Examples:**
```bash
# Get food products, page 0
GET http://localhost:8080/api/v1/productsapi/paginated/by-category?category=FOOD

# Get electronics sorted by ethical score descending, 15 items per page
GET http://localhost:8080/api/v1/productsapi/paginated/by-category?category=ELECTRONICS&sortBy=ethicalScore&sortDirection=DESC&size=15
```

---

#### Search Products by Name (Paginated)
**Endpoint:** `GET /api/v1/productsapi/paginated/search`

**Query Parameters:**
- `name` (required) - Product name or part of it (case-insensitive)
- `page` (optional, default: 0) - Page number
- `size` (optional, default: 10) - Number of products per page
- `sortBy` (optional, default: "productName") - Sort field
- `sortDirection` (optional, default: "ASC") - Sort direction

**Examples:**
```bash
# Search for products containing "organic"
GET http://localhost:8080/api/v1/productsapi/paginated/search?name=organic

# Search for "apple" products, sorted by transparency score, 20 items per page
GET http://localhost:8080/api/v1/productsapi/paginated/search?name=apple&sortBy=transparencyScore&sortDirection=DESC&size=20
```

---

#### Search Products by Brand (Paginated)
**Endpoint:** `GET /api/v1/productsapi/paginated/search-brand`

**Query Parameters:**
- `brand` (required) - Brand name or part of it (case-insensitive)
- `page` (optional, default: 0) - Page number
- `size` (optional, default: 10) - Number of products per page
- `sortBy` (optional, default: "brand") - Sort field
- `sortDirection` (optional, default: "ASC") - Sort direction

**Examples:**
```bash
# Search for products from "Nike" brand
GET http://localhost:8080/api/v1/productsapi/paginated/search-brand?brand=Nike

# Search for "Organic" in brand name, sorted by ethical score descending
GET http://localhost:8080/api/v1/productsapi/paginated/search-brand?brand=Organic&sortBy=ethicalScore&sortDirection=DESC
```

---

### 2. Score-Based Filtering Endpoints

#### Get Products by Ethical Score Range
**Endpoint:** `GET /api/v1/productsapi/filters/ethical-score-range`

**Query Parameters:**
- `minScore` (required) - Minimum ethical score (0-100)
- `maxScore` (required) - Maximum ethical score (0-100)
- `page` (optional, default: 0) - Page number
- `size` (optional, default: 10) - Number of products per page

**Examples:**
```bash
# Get products with ethical score between 70 and 90
GET http://localhost:8080/api/v1/productsapi/filters/ethical-score-range?minScore=70&maxScore=90

# Get highly ethical products (80-100), page 1, 20 items per page
GET http://localhost:8080/api/v1/productsapi/filters/ethical-score-range?minScore=80&maxScore=100&page=1&size=20
```

---

#### Get Products by Transparency Score Range
**Endpoint:** `GET /api/v1/productsapi/filters/transparency-score-range`

**Query Parameters:**
- `minScore` (required) - Minimum transparency score (0-100)
- `maxScore` (required) - Maximum transparency score (0-100)
- `page` (optional, default: 0) - Page number
- `size` (optional, default: 10) - Number of products per page

**Examples:**
```bash
# Get products with transparency score between 75 and 95
GET http://localhost:8080/api/v1/productsapi/filters/transparency-score-range?minScore=75&maxScore=95

# Get transparent products (85-100)
GET http://localhost:8080/api/v1/productsapi/filters/transparency-score-range?minScore=85&maxScore=100
```

---

#### Get High Ethical Score Products
**Endpoint:** `GET /api/v1/productsapi/filters/high-ethical-score`

**Query Parameters:**
- `minScore` (required) - Minimum ethical score threshold (0-100)
- `page` (optional, default: 0) - Page number
- `size` (optional, default: 10) - Number of products per page

**Examples:**
```bash
# Get products with ethical score >= 75
GET http://localhost:8080/api/v1/productsapi/filters/high-ethical-score?minScore=75

# Get highly ethical products (>= 80), page 2, 15 items per page
GET http://localhost:8080/api/v1/productsapi/filters/high-ethical-score?minScore=80&page=2&size=15
```

---

#### Get High Transparency Score Products
**Endpoint:** `GET /api/v1/productsapi/filters/high-transparency-score`

**Query Parameters:**
- `minScore` (required) - Minimum transparency score threshold (0-100)
- `page` (optional, default: 0) - Page number
- `size` (optional, default: 10) - Number of products per page

**Examples:**
```bash
# Get products with transparency score >= 80
GET http://localhost:8080/api/v1/productsapi/filters/high-transparency-score?minScore=80

# Get very transparent products (>= 90)
GET http://localhost:8080/api/v1/productsapi/filters/high-transparency-score?minScore=90
```

---

#### Get Products by Both Score Ranges
**Endpoint:** `GET /api/v1/productsapi/filters/score-range`

**Query Parameters:**
- `minEthicalScore` (required) - Minimum ethical score
- `maxEthicalScore` (required) - Maximum ethical score
- `minTransparencyScore` (required) - Minimum transparency score
- `maxTransparencyScore` (required) - Maximum transparency score
- `page` (optional, default: 0) - Page number
- `size` (optional, default: 10) - Number of products per page

**Examples:**
```bash
# Get products with both ethical and transparency scores between 70-90
GET http://localhost:8080/api/v1/productsapi/filters/score-range?minEthicalScore=70&maxEthicalScore=90&minTransparencyScore=70&maxTransparencyScore=90

# Get highly ethical and transparent products
GET http://localhost:8080/api/v1/productsapi/filters/score-range?minEthicalScore=80&maxEthicalScore=100&minTransparencyScore=80&maxTransparencyScore=100&page=0&size=25
```

---

#### Get Products by Category and Ethical Score
**Endpoint:** `GET /api/v1/productsapi/filters/category-and-ethical-score`

**Query Parameters:**
- `category` (required) - Product category
- `minEthicalScore` (required) - Minimum ethical score threshold
- `page` (optional, default: 0) - Page number
- `size` (optional, default: 10) - Number of products per page

**Examples:**
```bash
# Get ethical food products (score >= 75)
GET http://localhost:8080/api/v1/productsapi/filters/category-and-ethical-score?category=FOOD&minEthicalScore=75

# Get highly ethical clothing (score >= 85)
GET http://localhost:8080/api/v1/productsapi/filters/category-and-ethical-score?category=CLOTHING&minEthicalScore=85&size=20
```

---

### 3. Advanced Search Endpoint

#### Advanced Multi-Criteria Search
**Endpoint:** `POST /api/v1/productsapi/filters/advanced-search`

**Request Body (FilterCriteria):**
```json
{
  "minEthicalScore": 70,
  "maxEthicalScore": 100,
  "minTransparencyScore": 75,
  "maxTransparencyScore": 100,
  "brand": "Organic",
  "category": "FOOD",
  "ingredientName": "gluten",
  "sortBy": "ethicalScore",
  "sortDirection": "DESC",
  "pageNumber": 0,
  "pageSize": 10
}
```

**All fields are optional.** Include only the criteria you want to apply.

**Examples:**

1. **Search for organic food products with high ethical score:**
```bash
curl -X POST http://localhost:8080/api/v1/productsapi/filters/advanced-search \
  -H "Content-Type: application/json" \
  -d '{
    "brand": "Organic",
    "category": "FOOD",
    "minEthicalScore": 75,
    "sortBy": "ethicalScore",
    "sortDirection": "DESC",
    "pageSize": 10
  }'
```

2. **Search for products with specific ingredients:**
```bash
curl -X POST http://localhost:8080/api/v1/productsapi/filters/advanced-search \
  -H "Content-Type: application/json" \
  -d '{
    "ingredientName": "gluten-free",
    "minEthicalScore": 70,
    "pageNumber": 0,
    "pageSize": 15
  }'
```

3. **Complex multi-criteria search:**
```bash
curl -X POST http://localhost:8080/api/v1/productsapi/filters/advanced-search \
  -H "Content-Type: application/json" \
  -d '{
    "minEthicalScore": 75,
    "maxEthicalScore": 100,
    "minTransparencyScore": 80,
    "maxTransparencyScore": 100,
    "brand": "Fair Trade",
    "category": "CLOTHING",
    "sortBy": "transparencyScore",
    "sortDirection": "DESC",
    "pageNumber": 0,
    "pageSize": 20
  }'
```

---

## Sorting Options

### Available Sort Fields:
- `productName` - Sort by product name
- `ethicalScore` - Sort by ethical score
- `transparencyScore` - Sort by transparency score
- `brand` - Sort by brand name

### Sort Directions:
- `ASC` - Ascending order (default)
- `DESC` - Descending order

### Examples:
```bash
# Sort by name ascending (A-Z)
sortBy=productName&sortDirection=ASC

# Sort by ethical score descending (highest first)
sortBy=ethicalScore&sortDirection=DESC

# Sort by transparency score ascending (lowest first)
sortBy=transparencyScore&sortDirection=ASC
```

---

## Pagination Best Practices

### Query Parameter Limits:
- **page** number: Minimum 0 (no maximum, but should be practical)
- **pageSize**: 1-100 (requests outside this range will be rejected)
- Recommended pageSize values: 10, 20, 25, 50

### Example Pagination Patterns:

1. **Get first page (default):**
```bash
GET /api/v1/productsapi/paginated/all
```

2. **Get specific page:**
```bash
GET /api/v1/productsapi/paginated/all?page=2&size=10
```

3. **Using hasNext for infinite scroll:**
```
if (response.hasNext) {
  nextPage = currentPage + 1
  fetch(url + "?page=" + nextPage)
}
```

4. **Using totalPages for pagination UI:**
```
totalPages = response.totalPages
currentPage = response.currentPage
```

---

## FilterCriteria Field Descriptions

| Field | Type | Description | Example |
|-------|------|-------------|---------|
| `minEthicalScore` | Double | Minimum ethical score (0-100) | 70.0 |
| `maxEthicalScore` | Double | Maximum ethical score (0-100) | 95.0 |
| `minTransparencyScore` | Double | Minimum transparency score (0-100) | 75.0 |
| `maxTransparencyScore` | Double | Maximum transparency score (0-100) | 100.0 |
| `brand` | String | Brand name (partial match, case-insensitive) | "Organic" |
| `ingredientName` | String | Ingredient name (partial match) | "gluten-free" |
| `category` | String | Product category | "FOOD" |
| `sortBy` | String | Field to sort by | "ethicalScore" |
| `sortDirection` | String | Sort direction (ASC/DESC) | "DESC" |
| `pageNumber` | int | Page number (0-indexed) | 0 |
| `pageSize` | int | Items per page (1-100) | 10 |

---

## Error Handling

All endpoints may return the following error responses:

### 400 Bad Request
Invalid parameters (e.g., invalid page size, invalid category)

### 404 Not Found
No products found matching the criteria

### 500 Internal Server Error
Server-side error

**Error Response Format:**
```json
{
  "error": "Description of the error",
  "message": "Detailed error message"
}
```

---

## Frontend Integration Examples

### React Example (Fetching Paginated Products):
```javascript
const fetchProducts = async (page = 0, size = 10) => {
  try {
    const response = await fetch(
      `/api/v1/productsapi/paginated/all?page=${page}&size=${size}&sortBy=ethicalScore&sortDirection=DESC`
    );
    const data = await response.json();
    console.log(`Found ${data.totalElements} products`);
    console.log(`Page ${data.currentPage + 1} of ${data.totalPages}`);
    console.log(`Products:`, data.content);
    return data;
  } catch (error) {
    console.error('Error fetching products:', error);
  }
};
```

### JavaScript Example (Advanced Search):
```javascript
const advancedSearch = async (filters) => {
  const response = await fetch(
    '/api/v1/productsapi/filters/advanced-search',
    {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        minEthicalScore: 75,
        maxEthicalScore: 100,
        brand: filters.brand,
        category: filters.category,
        sortBy: 'ethicalScore',
        sortDirection: 'DESC',
        pageNumber: filters.page || 0,
        pageSize: filters.size || 10
      })
    }
  );
  return await response.json();
};
```

---

## Performance Notes

- **Pagination**: Recommended page sizes: 10-25 for better performance
- **Large result sets**: Use smaller page sizes and implement lazy loading on the frontend
- **Sorting**: Sorting by indexed fields (productName, scores) is optimized
- **Filtering**: Combined filters may impact performance with large datasets

---

## Version History

- **v1.0** - Initial release with pagination and sorting
- **v2.0** - Added advanced filtering by scores and brand
- **v3.0** - Added advanced multi-criteria search endpoint

