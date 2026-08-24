# Profile API Guide

All endpoints below require the existing `jwt` HTTP-only cookie. The application
context path is `/transparency-portal`.

## Profile screen

`GET /api/v1/profile`

Returns the logged-in user's profile, reviews, and activity counts:

```json
{
  "id": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "profileImageUrl": null,
  "memberSince": "August 2026",
  "reviews": [
    {
      "product": {
        "id": 1,
        "productName": "Example product",
        "imageUrl": "https://example.com/product.jpg",
        "brand": "Example brand"
      },
      "rating": 5,
      "comment": "A transparent product.",
      "postedAt": "2 weeks ago"
    }
  ],
  "reviewCount": 1,
  "savedProductCount": 2
}
```

`postedAt` is formatted as days, weeks, or months ago. `profileImageUrl` is
currently nullable so the frontend can display its default avatar.

## Saved products

`POST /api/v1/profile/saved-products/{productId}` saves a product for the
logged-in user. Saving the same product more than once is idempotent.

`DELETE /api/v1/profile/saved-products/{productId}` removes the product from
that user's saved list and returns `204 No Content`.

`GET /api/v1/profile/saved-products` returns the saved products in most-recently
saved order using the existing `ProductDTO` response shape.

## Reviews

`GET /api/v1/profile/reviews` returns the logged-in user's reviews using the
same review object shape shown in the profile response.

`POST /api/v1/profile/reviews/{productId}` creates the logged-in user's review.
Only one review per user and product is allowed.

```json
{
  "rating": 5,
  "comment": "A transparent product.",
  "tags": ["Effective", "Gentle"]
}
```

`rating` must be from 1 through 5 and `comment` must be non-blank and at most
2000 characters. `tags` is optional and accepts up to five labels. A duplicate
review returns `409 Conflict`.