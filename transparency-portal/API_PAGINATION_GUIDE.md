# Product API - Pagination, Search & Sorting Guide

This document provides a comprehensive guide to the enhanced Product API with pagination, sorting, and advanced filtering capabilities.

## Overview

The Product API now supports:
- **Pagination**: Retrieve products in pages with customizable page size
- **Sorting**: Sort results by multiple fields in ascending or descending order
- **Filtering**: Filter products by brand and category
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
      "ingredientSafety": "High",
      "informationQuality": "High",
      "evidenceQuality": "High",
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
  - Available values: `productName`, `brand`, `category`
- `sortDirection` (optional, default: "ASC") - Sort direction: `ASC` or `DESC`

**Examples:**
```bash
# Get all products, page 0, 10 items per page, sorted by product name ascending
GET http://localhost:8080/api/v1/productsapi/paginated/all

# Get page 2 with 20 items per page, sorted by product name descending
GET http://localhost:8080/api/v1/productsapi/paginated/all?page=2&size=20&sortBy=productName&sortDirection=DESC

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

# Get electronics sorted by brand, 15 items per page
GET http://localhost:8080/api/v1/productsapi/paginated/by-category?category=ELECTRONICS&sortBy=brand&sortDirection=ASC&size=15
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

# Search for "apple" products, sorted by product name, 20 items per page
GET http://localhost:8080/api/v1/productsapi/paginated/search?name=apple&sortBy=productName&sortDirection=DESC&size=20
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

# Search for "Organic" in brand name, sorted by brand
GET http://localhost:8080/api/v1/productsapi/paginated/search-brand?brand=Organic&sortBy=brand&sortDirection=ASC
```

---

### 2. Advanced Search Endpoint

#### Advanced Multi-Criteria Search
**Endpoint:** `POST /api/v1/productsapi/filters/advanced-search`

**Request Body (FilterCriteria):**
```json
{
  "brand": "Organic",
  "category": "FOOD",
  "ingredientName": "gluten",
  "sortBy": "productName",
  "sortDirection": "DESC",
  "pageNumber": 0,
  "pageSize": 10
}
```

**All fields are optional.** Include only the criteria you want to apply.

**Examples:**

1. **Search for organic food products:**
```bash
curl -X POST http://localhost:8080/api/v1/productsapi/filters/advanced-search \
  -H "Content-Type: application/json" \
  -d '{
    "brand": "Organic",
    "category": "FOOD",
    "sortBy": "productName",
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
    "pageNumber": 0,
    "pageSize": 15
  }'
```

3. **Complex multi-criteria search:**
```bash
curl -X POST http://localhost:8080/api/v1/productsapi/filters/advanced-search \
  -H "Content-Type: application/json" \
  -d '{
    "brand": "Fair Trade",
    "category": "CLOTHING",
    "sortBy": "brand",
    "sortDirection": "DESC",
    "pageNumber": 0,
    "pageSize": 20
  }'
```

---

## Sorting Options

### Available Sort Fields:
- `productName` - Sort by product name
- `brand` - Sort by brand name
- `category` - Sort by category

### Sort Directions:
- `ASC` - Ascending order (default)
- `DESC` - Descending order

### Examples:
```bash
# Sort by name ascending (A-Z)
sortBy=productName&sortDirection=ASC

# Sort by brand name ascending
sortBy=brand&sortDirection=ASC
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
| `brand` | String | Brand name (partial match, case-insensitive) | "Organic" |
| `ingredientName` | String | Ingredient name (partial match) | "gluten-free" |
| `category` | String | Product category | "FOOD" |
| `sortBy` | String | Field to sort by | "productName" |
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
      `/api/v1/productsapi/paginated/all?page=${page}&size=${size}&sortBy=productName&sortDirection=ASC`
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
        brand: filters.brand,
        category: filters.category,
        sortBy: 'productName',
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
