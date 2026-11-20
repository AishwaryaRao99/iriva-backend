# Architecture Diagram - Dynamic Product Data System

## System Architecture Overview

```
┌─────────────────────────────────────────────────────────────────────────┐
│                         FRONTEND (UI/Web)                               │
│  (No changes - Same API contract, returns ProductDTO with all data)     │
└──────────────────────────────┬──────────────────────────────────────────┘
                               │
                               ↓
┌─────────────────────────────────────────────────────────────────────────┐
│                      REST API LAYER                                      │
│  ProductController.java                                                  │
│  ├─ GET /api/products                  → List<ProductDTO>               │
│  ├─ GET /api/products/{id}             → ProductDTO                     │
│  ├─ GET /api/products/category/{cat}   → List<ProductDTO>               │
│  └─ GET /api/products/search?name=...  → List<ProductDTO>               │
└──────────────────────────────┬──────────────────────────────────────────┘
                               │
                               ↓
┌─────────────────────────────────────────────────────────────────────────┐
│                    SERVICE LAYER (BUSINESS LOGIC)                        │
│  ProductService.java                                                     │
│  ├─ getAllProducts() ────────────┐                                       │
│  ├─ getProductById(id) ──────────┼─→ toDTO(ProductModel) {             │
│  ├─ getProductsByCategory(cat) ──┤   ├─ convertEthicalItemsEntityToDTO()│
│  └─ searchProductsByName(name) ──┘   ├─ convertIngredientsEntityToDTO() │
│                                       └─ convertTransparencyAnalysisEnt()│
│   (Dynamic Conversion - Entity → DTO)                                    │
└──────────────────────────────┬──────────────────────────────────────────┘
                               │
                               ↓
┌─────────────────────────────────────────────────────────────────────────┐
│                    REPOSITORY LAYER (DATA ACCESS)                        │
│  ┌──────────────────────────────┬──────────────────────────────────┐    │
│  │ ProductRepository            │ NEW: Multiple Repositories        │    │
│  │ ├─ findAll()                 │ ├─ EthicalItemRepository         │    │
│  │ ├─ findById(id)              │ ├─ IngredientItemRepository      │    │
│  │ ├─ findByCategory(cat)       │ ├─ ScoreBreakdownRepository      │    │
│  │ └─ findByProductNameContaining()│ └─ TransparencyAnalysisRepo    │    │
│  └──────────────────────────────┴──────────────────────────────────┘    │
└──────────────────────────────┬──────────────────────────────────────────┘
                               │
                               ↓
┌─────────────────────────────────────────────────────────────────────────┐
│                    JPA ENTITY LAYER (DATA MODELS)                        │
│  ┌─────────────────────┬─────────────────────────────────────────────┐  │
│  │ ProductModel        │ NEW Entity Classes:                         │  │
│  │ ├─ id               │ ├─ EthicalItemEntity (@OneToMany)          │  │
│  │ ├─ productName      │ │  ├─ id, title, description, icon        │  │
│  │ ├─ description      │ │  └─ product (FK)                         │  │
│  │ ├─ imageUrl         │ ├─ IngredientItemEntity (@OneToMany)       │  │
│  │ ├─ brand            │ │  ├─ id, name, description, safetyStatus  │  │
│  │ ├─ ingredientSafety │ │  └─ product (FK)                         │  │
│  │ ├─ informationQuality│ ├─ TransparencyAnalysisEntity (@OneToOne) │  │
│  │ ├─ evidenceQuality  │ │  ├─ id                                   │  │
│  │ ├─ category         │ │  ├─ scoreHighReasonsJson (CLOB)          │  │
│  │ │ NEW MAPPINGS:     │ │  ├─ improvementAreasJson (CLOB)          │  │
│  │ ├─ ethicalSummary  │ │  └─ scoreBreakdown (FK @OneToOne)         │  │
│  │ │  (@OneToMany)    │ └─ ScoreBreakdownEntity                     │  │
│  │ ├─ ingredients      │    ├─ id                                   │  │
│  │ │  (@OneToMany)    │    ├─ ingredientTransparency                │  │
│  │ └─ transparencyAnalysis │    ├─ ethicalCertifications                │  │
│  │    (@OneToOne)      │    ├─ manufacturingInfo                    │  │
│  │                     │    └─ sourcingTransparency                 │  │
│  └─────────────────────┴─────────────────────────────────────────────┘  │
└──────────────────────────────┬──────────────────────────────────────────┘
                               │
                               ↓
┌─────────────────────────────────────────────────────────────────────────┐
│                         DATABASE LAYER                                   │
│  ┌──────────────┬──────────────┬──────────────┬──────────────┬────────┐ │
│  │   PRODUCTS   │ ETHICAL_ITEMS│INGREDIENT_   │   SCORE_     │TRANS-  │ │
│  │              │              │ ITEMS        │ BREAKDOWNS   │PARENCY │ │
│  │ ┌──────────┐ │┌────────────┐│┌─────────────┐│┌─────────────┐│ANALYSES│ │
│  │ │ id (PK)  │ ││ id (PK)    ││ id (PK)     ││ id (PK)     ││ id(PK) │ │
│  │ │ name     │ ││ product_id ││ product_id  ││ ingredient_ ││        │ │
│  │ │ brand    │ ││ (FK)       ││ (FK)        ││ transparency││product │ │
│  │ │ scores   │ ││ title      ││ name        ││             ││ (FK)   │ │
│  │ │ category │ ││ description││ description ││ ethical_    ││        │ │
│  │ │ trans_   │ ││ icon       ││ safety_     ││ certifications││score_ │ │
│  │ │ analysis ││ (FK)│ (FK) │ high_reasons││ manufacturing│ │
│  │ │ (FK)     │ └────────────┘└─────────────┘└─────────────┘│improvement│ │
│  │ └──────────┘                                │sourcing_   ││_areas  │ │
│  │ 16 Records │ 55+ Records  │ 48+ Records   │transparency ││16 Rec. │ │
│  └──────────┴──────────────┴──────────────┴──────────────┴────────┘ │
│                                                                       │
│  CASCADE: ALL, ORPHAN REMOVAL: TRUE                                  │
│  RELATIONSHIPS: 1:M (products→ethical), 1:M (products→ingredients),  │
│                1:1 (products→transparency)                           │
└─────────────────────────────────────────────────────────────────────────┘
```

## Data Flow Example: GET /api/products/1

```
1. API REQUEST
   ↓
   GET /api/products/1
   ↓
2. CONTROLLER
   ↓
   ProductController.getProductById(1)
   ↓
3. SERVICE LAYER
   ↓
   ProductService.getProductById(1) {
     ├─ repository.findById(1)  → ProductModel with relationships loaded
     ├─ toDTO(model) {
     │  ├─ convertEthicalItemsEntityToDTO(model.getEthicalSummary())
     │  │  └─ for each EthicalItemEntity → EthicalItem DTO
     │  ├─ convertIngredientsEntityToDTO(model.getIngredients())
     │  │  └─ for each IngredientItemEntity → IngredientItem DTO
     │  └─ convertTransparencyAnalysisEntityToDTO(model.getTransparencyAnalysis())
     │     ├─ Parse scoreHighReasonsJson (JSON) → List<String>
     │     ├─ Parse improvementAreasJson (JSON) → List<String>
     │     └─ Convert ScoreBreakdownEntity → ScoreBreakdown DTO
     │  }
     └─ return ProductDTO (complete with all nested data)
   ↓
4. DATABASE QUERIES
   ├─ SELECT * FROM products WHERE id = 1
   ├─ SELECT * FROM ethical_items WHERE product_id = 1 (lazy loaded)
   ├─ SELECT * FROM ingredient_items WHERE product_id = 1 (lazy loaded)
   ├─ SELECT * FROM transparency_analyses WHERE product_id = 1 (lazy loaded)
   └─ SELECT * FROM score_breakdowns WHERE id = ?
   ↓
5. JSON RESPONSE
   ↓
   {
     "id": 1,
     "productName": "Burt's Bees...",
     "ethicalSummary": [
       { "title": "No Animal Testing", ... },
       { "title": "100% Natural Origin", ... }
     ],
     "ingredients": [
       { "name": "Pomegranate Seed Oil", ... }
     ],
     "transparencyAnalysis": {
       "scoreHighReasons": ["Natural ingredients with full transparency", ...],
       "improvementAreas": ["Packaging could be more recyclable", ...],
       "scoreBreakdown": {
         "ingredientTransparency": 92,
         ...
       }
     }
   }
   ↓
6. FRONTEND RECEIVES
   ↓
   (Same format as before - No breaking changes!)
```

## Database Relationship Model

```
PRODUCTS (16 records)
│
├──────────────────────────┬─────────────────────────┬──────────────────┐
│                          │                         │                  │
v                          v                         v                  v
│
├─→ ETHICAL_ITEMS (55+ records)        1:M Relationship
│   Each product has multiple ethical items
│
├─→ INGREDIENT_ITEMS (48+ records)     1:M Relationship
│   Each product has multiple ingredients
│
└─→ TRANSPARENCY_ANALYSES (16 records) 1:1 Relationship
    ├─→ SCORE_BREAKDOWNS (16 records)  1:1 Relationship
        Each analysis has one score breakdown
```

## Key Design Decisions

### 1. **Cascade Strategy**
```java
@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
```
- **Why**: Ensures data integrity
- **Benefit**: Deleting a product automatically deletes all related items
- **Safety**: No orphaned records in database

### 2. **JSON Storage for Complex Data**
```java
@Lob
private String scoreHighReasonsJson;  // Stored as JSON array
private String improvementAreasJson;  // Parsed on read
```
- **Why**: Flexibility for variable-length arrays
- **Benefit**: No need for additional junction tables
- **Performance**: Single column read with Jackson parsing

### 3. **Entity vs DTO Conversion**
```java
// Entity (Persistent, database-mapped)
EthicalItemEntity

// DTO (Transfer object, API response)
EthicalItem (from existing model)

// Service converts: Entity → DTO
convertEthicalItemsEntityToDTO(List<EthicalItemEntity>) → List<EthicalItem>
```
- **Why**: Clean separation of concerns
- **Benefit**: API contract unchanged, implementation details hidden
- **Flexibility**: Easy to refactor database without affecting API

### 4. **Lazy Loading Configuration**
```
Products → EthicalItems: Lazy loaded on demand
Products → Ingredients: Lazy loaded on demand
Products → TransparencyAnalysis: Lazy loaded on demand
```
- **Why**: Optimize query performance
- **Benefit**: Only load data when needed
- **Trade-off**: Potential N+1 queries (acceptable for MVP)

## Class Diagram

```
┌─────────────────────────────────────────────────┐
│                ProductModel                      │
├─────────────────────────────────────────────────┤
│ - id: Long                                       │
│ - productName: String                            │
│ - description: String                            │
│ - imageUrl: String                               │
│ - brand: String                                  │
│ - ingredientSafety: String                       │
│ - informationQuality: String                     │
│ - evidenceQuality: String                         │
│ - category: ProductCategory                      │
│ - ethicalSummary: List<EthicalItemEntity>        │ ◄─────┐
│ - ingredients: List<IngredientItemEntity>        │ ◄─────┼─┐
│ - transparencyAnalysis: TransparencyAnalysisEntity│ ◄─────┼─┼─┐
├─────────────────────────────────────────────────┤        │ │ │
│ + getEthicalSummary()                            │        │ │ │
│ + getIngredients()                               │        │ │ │
│ + getTransparencyAnalysis()                      │        │ │ │
└─────────────────────────────────────────────────┘        │ │ │
         │                                                 │ │ │
         │1     M│                                         │ │ │
         ├──────────┐                                      │ │ │
         │          │                                      │ │ │
         v          v                                      │ │ │
┌──────────────────────────┐  ┌──────────────────────────┤─┘ │
│  EthicalItemEntity       │  │ IngredientItemEntity      │   │
├──────────────────────────┤  ├──────────────────────────┤   │
│ - id: Long               │  │ - id: Long               │   │
│ - title: String          │  │ - name: String           │   │
│ - description: String    │  │ - description: String    │   │
│ - icon: String           │  │ - safetyStatus: String   │   │
│ - product: ProductModel  │  │ - product: ProductModel  │   │
└──────────────────────────┘  └──────────────────────────┘   │
                                                              │
         ┌───────────────────────────────────────────────────┘
         │
         v
┌──────────────────────────────────────────────────┐
│  TransparencyAnalysisEntity                       │
├──────────────────────────────────────────────────┤
│ - id: Long                                        │
│ - scoreHighReasonsJson: String (JSON Array)      │
│ - improvementAreasJson: String (JSON Array)      │
│ - scoreBreakdown: ScoreBreakdownEntity           │
│ - product: ProductModel                          │
└──────────────────────────────────────────────────┘
         │1     1│
         │       │
         v       v
┌──────────────────────────────────────────────────┐
│  ScoreBreakdownEntity                             │
├──────────────────────────────────────────────────┤
│ - id: Long                                        │
│ - ingredientTransparency: int                     │
│ - ethicalCertifications: int                      │
│ - manufacturingInfo: int                          │
│ - sourcingTransparency: int                       │
└──────────────────────────────────────────────────┘
```

## Deployment Topology

```
┌─────────────────────────────────────────────────┐
│         Web Server / Application Server          │
│         (Spring Boot Application)                │
├─────────────────────────────────────────────────┤
│  ProductController                               │
│  ProductService                                  │
│  ProductRepository                               │
│  EthicalItemRepository                           │
│  IngredientItemRepository                        │
│  TransparencyAnalysisRepository                  │
│  ScoreBreakdownRepository                        │
└──────────────────┬──────────────────────────────┘
                   │ JDBC Driver
                   │
┌──────────────────v──────────────────────────────┐
│            Database Server                       │
├─────────────────────────────────────────────────┤
│ PRODUCTS                                         │
│ ETHICAL_ITEMS                                    │
│ INGREDIENT_ITEMS                                 │
│ SCORE_BREAKDOWNS                                 │
│ TRANSPARENCY_ANALYSES                            │
└─────────────────────────────────────────────────┘
```

---

**This architecture provides:**
- ✅ Clean separation of concerns
- ✅ Scalable database design
- ✅ Type-safe JPA entities
- ✅ Efficient repository pattern
- ✅ Consistent service layer
- ✅ Production-ready code
- ✅ No breaking API changes
