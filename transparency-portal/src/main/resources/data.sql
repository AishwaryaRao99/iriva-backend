
--------------------------------------------- USERS TABLE -------------------------------------------------------
INSERT INTO users (username, display_name, role, email, password, created_at, updated_at) 
VALUES ('john_doe', 'John Doe', 'ROLE_USER', 'john@example.com', 
        '$2a$12$O718yic3jvorcrL9cmaRjOJWsr0W0UIFirtysqMsM9mrzgwJ1xa0.', 
        NOW(), NOW());

INSERT INTO users (username, display_name, role, email, password, created_at, updated_at) 
VALUES ('admin', 'Admin', 'ROLE_ADMIN', 'admin@example.com', 
        '$2a$12$O718yic3jvorcrL9cmaRjOJWsr0W0UIFirtysqMsM9mrzgwJ1xa0.', 
        NOW(), NOW());

-- Placeholder profile user used by the frontend profile screen.
INSERT INTO users (username, display_name, role, email, password, created_at, updated_at)
VALUES ('sarah_mitchell', 'Sarah Mitchell', 'ROLE_USER', 'sarah.mitchell@email.com',
        '$2a$12$O718yic3jvorcrL9cmaRjOJWsr0W0UIFirtysqMsM9mrzgwJ1xa0.',
        DATEADD('MONTH', -17, NOW()), NOW());

----------------------------------------------------- PRODUCTS TABLE -----------------------------------------------------------
-- ========== SKINCARE PRODUCTS ==========

-- Product 1: Burt's Bees Very Volumizing Pomegranate Shampoo
INSERT INTO PRODUCTS (product_name, DESCRIPTION, IMAGE_URL, BRAND, ETHICAL_SCORE, TRANSPARENCY_SCORE, CATEGORY)
VALUES ('Burt''s Bees Very Volumizing Pomegranate Shampoo',
'Natural shampoo made with pomegranate seed oil, free of sulfates and parabens. Made with 99.6% natural ingredients.',
	'https://images.unsplash.com/photo-1556228578-8c89e6adf883?w=500&q=80',
	'Burt''s Bees', 9.1, 9.3, 'SKINCARE');


INSERT INTO ETHICAL_ITEMS (product_id, title, description, icon) VALUES
(1, 'No Animal Testing', 'Certified cruelty-free by Leaping Bunny', 'heart-icon'),
(1, '100% Natural Origin', 'All ingredients naturally derived', 'leaf-icon'),
(1, 'Environmentally Friendly', 'Biodegradable and eco-conscious packaging', 'globe-icon');

INSERT INTO INGREDIENT_ITEMS (product_id, name, description, safety_status) VALUES
(1, 'Pomegranate Seed Oil', 'Rich in antioxidants for hair vitality', 'Safe'),
(1, 'Honey', 'Natural moisturizer and strengthening agent', 'Safe'),
(1, 'Herbal Extracts', 'Blend of natural plant extracts', 'Safe');

INSERT INTO SCORE_BREAKDOWNS (ingredient_transparency, ethical_certifications, manufacturing_info, sourcing_transparency)
VALUES (92, 94, 90, 91);

INSERT INTO TRANSPARENCY_ANALYSES (score_breakdown_id, score_high_reasons_json, improvement_areas_json)
VALUES (1, '["Natural ingredients with full transparency", "Certified by Leaping Bunny for cruelty-free practices", "Manufacturing location disclosed", "Sustainable sourcing practices documented"]',
'["Packaging could be more recyclable", "Limited supply chain transparency for raw materials"]');


UPDATE PRODUCTS SET transparency_analysis_id = 1 WHERE id = 1;

-- Product 2: CeraVe Moisturizing Cream
INSERT INTO PRODUCTS (product_name, DESCRIPTION, IMAGE_URL, BRAND, ETHICAL_SCORE, TRANSPARENCY_SCORE, CATEGORY)
VALUES ('CeraVe Moisturizing Cream',
'Dermatologist-developed daily moisturizing cream with three essential ceramides and hyaluronic acid. Fragrance-free and non-comedogenic.',
'https://images.unsplash.com/photo-1616986953793-2e6159b78580?w=400',
'CeraVe', 8.8, 9.1, 'SKINCARE');


INSERT INTO ETHICAL_ITEMS (product_id, title, description, icon) VALUES
(2, 'Dermatologist Approved', 'Tested and recommended by dermatologists', 'doctor-icon'),
(2, 'Hypoallergenic Formula', 'Safe for sensitive skin', 'shield-icon'),
(2, 'Fragrance Free', 'No added fragrances or irritants', 'check-icon');

INSERT INTO INGREDIENT_ITEMS (product_id, name, description, safety_status) VALUES
(2, 'Ceramides', 'Essential skin barrier repair', 'Safe'),
(2, 'Hyaluronic Acid', 'Powerful moisturizing ingredient', 'Safe'),
(2, 'Niacinamide', 'Vitamin B3 for skin strengthening', 'Safe');

INSERT INTO SCORE_BREAKDOWNS (ingredient_transparency, ethical_certifications, manufacturing_info, sourcing_transparency)
VALUES (88, 85, 90, 87);

INSERT INTO TRANSPARENCY_ANALYSES (score_breakdown_id, score_high_reasons_json, improvement_areas_json)
VALUES (2, '["Complete INCI naming of ingredients", "Dermatologist tested and approved", "Manufacturing standards disclosed", "Paraben-free and cruelty-free commitment"]',
'["Limited information on ingredient sourcing", "Parent company practices could be more transparent"]');


UPDATE PRODUCTS SET transparency_analysis_id = 2 WHERE id = 2;

-- Product 3: Drunk Elephant C-Firma Fresh Vitamin C Serum
INSERT INTO PRODUCTS (product_name, DESCRIPTION, IMAGE_URL, BRAND, ETHICAL_SCORE, TRANSPARENCY_SCORE, CATEGORY)
VALUES ('Drunk Elephant C-Firma Fresh Vitamin C Serum',
'Potent vitamin C serum designed to brighten the complexion and fight environmental stressors. Clean, cruelty-free beauty.',
'https://images.unsplash.com/photo-1608571423902-eed4a5ad8108?w=500&q=80',
'Drunk Elephant', 8.9, 9.4, 'SKINCARE');


INSERT INTO ETHICAL_ITEMS (product_id, title, description, icon) VALUES
(3, 'Cruelty-Free Certified', 'Not tested on animals, certified by Leaping Bunny', 'bunny-icon'),
(3, 'Clean Beauty', 'Free of harmful chemicals and toxins', 'leaf-icon'),
(3, 'High Transparency', 'Ingredient sourcing and effects clearly documented', 'eye-icon');

INSERT INTO INGREDIENT_ITEMS (product_id, name, description, safety_status) VALUES
(3, 'Vitamin C', 'Powerful antioxidant for brightening', 'Safe'),
(3, 'Ferulic Acid', 'Enhances antioxidant benefits', 'Safe'),
(3, 'Vitamin E', 'Protective antioxidant ingredient', 'Safe');

INSERT INTO SCORE_BREAKDOWNS (ingredient_transparency, ethical_certifications, manufacturing_info, sourcing_transparency)
VALUES (94, 92, 93, 92);

INSERT INTO TRANSPARENCY_ANALYSES (score_breakdown_id, score_high_reasons_json, improvement_areas_json)
VALUES (3, '["Comprehensive ingredient list with benefits explained", "Cruelty-free and vegan commitment verified", "Manufacturing process disclosed", "Supply chain transparency documented"]',
'["Premium pricing limits accessibility", "Packaging could improve sustainability"]');


UPDATE PRODUCTS SET transparency_analysis_id = 3 WHERE id = 3;

-- Product 4: Dr. Bronner's 18-in-1 Hemp Pure Castile Soap
INSERT INTO PRODUCTS (product_name, DESCRIPTION, IMAGE_URL, BRAND, ETHICAL_SCORE, TRANSPARENCY_SCORE, CATEGORY)
VALUES ('Dr. Bronner''s 18-in-1 Hemp Pure Castile Soap',
'Multi-purpose, plant-based soap made with organic oils. Fair trade certified, vegan, and completely transparent supply chain.',
'https://images.unsplash.com/photo-1650964336602-f60274c5a94d?w=500&q=80',
'Dr. Bronner''s', 9.6, 9.8, 'SKINCARE');


INSERT INTO ETHICAL_ITEMS (product_id, title, description, icon) VALUES
(4, 'Fair Trade Certified', 'All ingredients sourced through fair trade practices', 'handshake-icon'),
(4, '100% Vegan Formula', 'No animal-derived ingredients or by-products', 'leaf-icon'),
(4, 'Transparent Supply Chain', 'Complete traceability from source to consumer', 'link-icon'),
(4, 'Organic Ingredients', 'USDA certified organic oils', 'certified-icon');

INSERT INTO INGREDIENT_ITEMS (product_id, name, description, safety_status) VALUES
(4, 'Organic Hemp Oil', 'Nourishing and sustainable oil', 'Safe'),
(4, 'Organic Coconut Oil', 'Cleansing natural oil', 'Safe'),
(4, 'Organic Jojoba Oil', 'Skin-nourishing oil', 'Safe'),
(4, 'Organic Olive Oil', 'Moisturizing base oil', 'Safe');

INSERT INTO SCORE_BREAKDOWNS (ingredient_transparency, ethical_certifications, manufacturing_info, sourcing_transparency)
VALUES (98, 96, 95, 97);

INSERT INTO TRANSPARENCY_ANALYSES (score_breakdown_id, score_high_reasons_json, improvement_areas_json)
VALUES (4, '["Complete ingredient transparency with sourcing location", "Fair Trade certified for all ingredients", "USDA Organic certification verified", "Manufacturing practices fully disclosed", "Vegan and cruelty-free certification", "Biodegradable formula"]',
'["Could provide more detailed sustainability metrics", "Packaging improvements still possible"]');


UPDATE PRODUCTS SET transparency_analysis_id = 4 WHERE id = 4;

-- ========== FOOD PRODUCTS ==========

-- Product 5: Fair Trade Certified Organic Coffee
INSERT INTO PRODUCTS (product_name, DESCRIPTION, IMAGE_URL, BRAND, ETHICAL_SCORE, TRANSPARENCY_SCORE, CATEGORY)
VALUES ('Fair Trade Certified Organic Coffee',
'Single-origin organic coffee beans from Ethiopian smallholder farmers. Fair trade certified with full traceability.',
'http://localhost:8080/transparency-portal/images/products/coffee.jpg',
'Equal Exchange', 9.3, 9.5, 'FOOD');


INSERT INTO ETHICAL_ITEMS (product_id, title, description, icon) VALUES
(5, 'Fair Trade Certified', 'Supports smallholder farmers with fair wages', 'handshake-icon'),
(5, 'USDA Organic Certified', 'No synthetic pesticides or fertilizers', 'leaf-icon'),
(5, 'Single-Origin Sourcing', 'From Ethiopian highlands at sustainable altitude', 'globe-icon'),
(5, 'Full Traceability', 'Track your coffee from farm to cup', 'link-icon');

INSERT INTO INGREDIENT_ITEMS (product_id, name, description, safety_status) VALUES
(5, 'Organic Coffee Beans', 'Premium Arabica beans from Ethiopia', 'Safe'),
(5, 'Antioxidants', 'Natural compounds from the coffee plant', 'Safe');

INSERT INTO SCORE_BREAKDOWNS (ingredient_transparency, ethical_certifications, manufacturing_info, sourcing_transparency)
VALUES (91, 95, 88, 96);

INSERT INTO TRANSPARENCY_ANALYSES (score_breakdown_id, score_high_reasons_json, improvement_areas_json)
VALUES (5, '["Fair Trade certification with farmer direct relationships", "USDA Organic certification", "Farm location and farmer names disclosed", "Roasting process details provided", "Environmental impact metrics shared"]',
'["Packaging could include more sustainability information", "Could provide pricing breakdown between farmer and company"]');


UPDATE PRODUCTS SET transparency_analysis_id = 5 WHERE id = 5;

-- Product 6: Organic Raw Almond Butter
INSERT INTO PRODUCTS (product_name, DESCRIPTION, IMAGE_URL, BRAND, ETHICAL_SCORE, TRANSPARENCY_SCORE, CATEGORY)
VALUES ('Organic Raw Almond Butter',
'Cold-pressed almonds from certified organic orchards. No added sugars, oils, or salt. Verified sustainable sourcing.',
'http://localhost:8080/transparency-portal/images/products/almond.jpg',
'Barney Butter', 8.7, 8.9, 'FOOD');


INSERT INTO ETHICAL_ITEMS (product_id, title, description, icon) VALUES
(6, 'Cold-Pressed Process', 'Preserves nutrients without heating', 'snowflake-icon'),
(6, 'Certified Organic', 'No synthetic chemicals used in cultivation', 'certified-icon'),
(6, 'No Additives', 'Pure almonds only, no added ingredients', 'check-icon');

INSERT INTO INGREDIENT_ITEMS (product_id, name, description, safety_status) VALUES
(6, 'Raw Almonds', 'From certified organic orchards', 'Safe'),
(6, 'Vitamin E', 'Natural antioxidant from almonds', 'Safe'),
(6, 'Healthy Fats', 'Monounsaturated and polyunsaturated fats', 'Safe');

INSERT INTO SCORE_BREAKDOWNS (ingredient_transparency, ethical_certifications, manufacturing_info, sourcing_transparency)
VALUES (85, 88, 86, 87);

INSERT INTO TRANSPARENCY_ANALYSES (score_breakdown_id, score_high_reasons_json, improvement_areas_json)
VALUES (6, '["Organic certification from USDA", "Cold-press manufacturing process disclosed", "Farm locations in California disclosed", "Nutritional benefits clearly labeled"]',
'["More details on water usage in almond production", "Could expand supply chain transparency"]');


UPDATE PRODUCTS SET transparency_analysis_id = 6 WHERE id = 6;

-- Product 7: Rainforest Alliance Certified Chocolate Bar
INSERT INTO PRODUCTS (product_name, DESCRIPTION, IMAGE_URL, BRAND, ETHICAL_SCORE, TRANSPARENCY_SCORE, CATEGORY)
VALUES ('Rainforest Alliance Certified Chocolate Bar',
'Single-origin dark chocolate (72% cacao) from responsibly managed farms. Supports forest conservation.',
'http://localhost:8080/transparency-portal/images/products/chocolate.jpg',
'Tony''s Chocolonely', 9.2, 9.4, 'FOOD');


INSERT INTO ETHICAL_ITEMS (product_id, title, description, icon) VALUES
(7, 'Rainforest Alliance Certified', 'Forests and wildlife protected', 'leaf-icon'),
(7, 'Fair Wage Guarantee', 'Ensures cocoa farmers receive fair compensation', 'handshake-icon'),
(7, 'Single-Origin Cacao', 'Premium cacao from Ecuador', 'globe-icon'),
(7, 'Forest Conservation', 'Portion of sales goes to reforestation', 'tree-icon');

INSERT INTO INGREDIENT_ITEMS (product_id, name, description, safety_status) VALUES
(7, 'Cacao Beans (72%)', 'Premium single-origin from Ecuador', 'Safe'),
(7, 'Cacao Butter', 'Natural cocoa butter for smooth texture', 'Safe'),
(7, 'Organic Sugar', 'Fair Trade certified sugar', 'Safe'),
(7, 'Vanilla', 'Madagascar vanilla for flavor', 'Safe');

INSERT INTO SCORE_BREAKDOWNS (ingredient_transparency, ethical_certifications, manufacturing_info, sourcing_transparency)
VALUES (90, 94, 91, 93);

INSERT INTO TRANSPARENCY_ANALYSES (score_breakdown_id, score_high_reasons_json, improvement_areas_json)
VALUES (7, '["Rainforest Alliance certification verified", "Direct relationships with cocoa farmers documented", "Fair Trade pricing model explained", "Environmental impact metrics published", "Forest conservation program details provided"]',
'["More granular supply chain data could be public", "Packaging materials could be more detailed"]');


UPDATE PRODUCTS SET transparency_analysis_id = 7 WHERE id = 7;

-- Product 8: USDA Organic Certified Blueberries (renamed from Driscoll's)
INSERT INTO PRODUCTS (product_name, DESCRIPTION, IMAGE_URL, BRAND, ETHICAL_SCORE, TRANSPARENCY_SCORE, CATEGORY)
VALUES ('USDA Organic Certified Blueberries',
'Freshly harvested organic blueberries from family farms with complete supply chain transparency. Non-GMO verified.',
'http://localhost:8080/transparency-portal/images/products/blueberries.jpg',
'Nature''s Harvest', 8.8, 8.7, 'FOOD');


INSERT INTO ETHICAL_ITEMS (product_id, title, description, icon) VALUES
(8, 'USDA Organic Certified', 'No synthetic pesticides used', 'certified-icon'),
(8, 'Non-GMO Verified', 'Not genetically modified', 'check-icon'),
(8, 'Family Farm Sourcing', 'Supports independent family farmers', 'handshake-icon');

INSERT INTO INGREDIENT_ITEMS (product_id, name, description, safety_status) VALUES
(8, 'Fresh Blueberries', 'Organically grown from family orchards', 'Safe'),
(8, 'Anthocyanins', 'Natural antioxidants from blueberries', 'Safe');

INSERT INTO SCORE_BREAKDOWNS (ingredient_transparency, ethical_certifications, manufacturing_info, sourcing_transparency)
VALUES (84, 87, 83, 85);

INSERT INTO TRANSPARENCY_ANALYSES (score_breakdown_id, score_high_reasons_json, improvement_areas_json)
VALUES (8, '["USDA Organic certification", "Non-GMO Project Verified", "Family farm partnerships disclosed", "Harvest date and handling information provided"]',
'["Could provide more detailed pesticide-free verification", "Supply chain timing information could be more transparent"]');


UPDATE PRODUCTS SET transparency_analysis_id = 8 WHERE id = 8;

-- ========== CLEANING PRODUCTS ==========

-- Product 9: Ecos Hypoallergenic All-Purpose Cleaner
INSERT INTO PRODUCTS (product_name, DESCRIPTION, IMAGE_URL, BRAND, ETHICAL_SCORE, TRANSPARENCY_SCORE, CATEGORY)
VALUES ('Ecos Hypoallergenic All-Purpose Cleaner',
'Plant-based, non-toxic all-purpose cleaner. Vegan, cruelty-free, and biodegradable. EPA certified Safer Choice product.',
'http://localhost:8080/transparency-portal/images/products/spray.jpg',
'ECOS', 8.9, 9.2, 'CLEANING');


INSERT INTO ETHICAL_ITEMS (product_id, title, description, icon) VALUES
(9, 'EPA Safer Choice Certified', 'Meets EPA environmental and health standards', 'certified-icon'),
(9, 'Plant-Based Formula', 'Made from renewable plant resources', 'leaf-icon'),
(9, 'Biodegradable', 'Safe for aquatic ecosystems', 'water-icon'),
(9, 'Cruelty-Free', 'No animal testing, certified vegan', 'heart-icon');

INSERT INTO INGREDIENT_ITEMS (product_id, name, description, safety_status) VALUES
(9, 'Plant-Based Surfactants', 'Sustainable cleaning agents', 'Safe'),
(9, 'Citric Acid', 'Natural deodorizer and cleaner', 'Safe'),
(9, 'Water', 'Base ingredient for dilution', 'Safe');

INSERT INTO SCORE_BREAKDOWNS (ingredient_transparency, ethical_certifications, manufacturing_info, sourcing_transparency)
VALUES (87, 91, 88, 89);

INSERT INTO TRANSPARENCY_ANALYSES (score_breakdown_id, score_high_reasons_json, improvement_areas_json)
VALUES (9, '["EPA Safer Choice certification", "Plant-based ingredient sourcing disclosed", "Biodegradable formula verified", "Manufacturing location transparent", "Vegan certification confirmed"]',
'["Packaging could be 100% recyclable", "More supply chain details on plant sourcing"]');


UPDATE PRODUCTS SET transparency_analysis_id = 9 WHERE id = 9;

-- Product 10: Seventh Generation Free & Clear Laundry Detergent
INSERT INTO PRODUCTS (product_name, DESCRIPTION, IMAGE_URL, BRAND, ETHICAL_SCORE, TRANSPARENCY_SCORE, CATEGORY)
VALUES ('Seventh Generation Free & Clear Laundry Detergent',
'Hypoallergenic laundry detergent free of dyes and perfumes. Plant-derived ingredients, biodegradable, and cruelty-free.',
'http://localhost:8080/transparency-portal/images/products/detergent.jpg',
'Seventh Generation', 9.0, 9.1, 'CLEANING');


INSERT INTO ETHICAL_ITEMS (product_id, title, description, icon) VALUES
(10, 'Hypoallergenic', 'Free of dyes, perfumes, and optical brighteners', 'shield-icon'),
(10, 'Biodegradable Formula', 'Breaks down safely in water systems', 'water-icon'),
(10, 'Cruelty-Free Certified', 'Not tested on animals', 'bunny-icon'),
(10, 'Plant-Derived', 'Ingredients sourced from plants', 'leaf-icon');

INSERT INTO INGREDIENT_ITEMS (product_id, name, description, safety_status) VALUES
(10, 'Plant-Based Surfactants', 'From renewable plant sources', 'Safe'),
(10, 'Enzymes', 'Natural catalysts for stain removal', 'Safe'),
(10, 'Minerals', 'Water conditioning agents', 'Safe');

INSERT INTO SCORE_BREAKDOWNS (ingredient_transparency, ethical_certifications, manufacturing_info, sourcing_transparency)
VALUES (89, 92, 89, 90);

INSERT INTO TRANSPARENCY_ANALYSES (score_breakdown_id, score_high_reasons_json, improvement_areas_json)
VALUES (10, '["Free from synthetic dyes and perfumes", "Cruelty-free and vegan certification", "Biodegradable ingredient list", "Manufacturing processes disclosed", "Plant-sourced ingredients documented"]',
'["Packaging recyclability could be improved", "More transparent water usage information"]');


UPDATE PRODUCTS SET transparency_analysis_id = 10 WHERE id = 10;

-- Product 11: Nellie's All Natural Laundry Soda
INSERT INTO PRODUCTS (product_name, DESCRIPTION, IMAGE_URL, BRAND, ETHICAL_SCORE, TRANSPARENCY_SCORE, CATEGORY)
VALUES ('Nellie''s All Natural Laundry Soda',
'Zero-waste laundry detergent concentrate made from naturally sourced minerals. Biodegradable and septic safe.',
'http://localhost:8080/transparency-portal/images/products/spray.jpg',
'Nellie''s', 9.1, 9.0, 'CLEANING');


INSERT INTO ETHICAL_ITEMS (product_id, title, description, icon) VALUES
(11, 'Zero-Waste Packaging', 'Plastic-free, compostable packaging', 'leaf-icon'),
(11, 'Natural Minerals Only', 'No synthetic chemicals or fillers', 'secure-icon'),
(11, 'Septic Safe', 'Won''t damage septic systems', 'water-icon'),
(11, 'Eco-Friendly', 'Minimal environmental impact', 'globe-icon');

INSERT INTO INGREDIENT_ITEMS (product_id, name, description, safety_status) VALUES
(11, 'Washing Soda', 'Natural mineral for cleaning power', 'Safe'),
(11, 'Borax', 'Natural mineral booster', 'Safe'),
(11, 'Natural Soap', 'Plant-based cleansing agent', 'Safe');

INSERT INTO SCORE_BREAKDOWNS (ingredient_transparency, ethical_certifications, manufacturing_info, sourcing_transparency)
VALUES (91, 89, 90, 92);

INSERT INTO TRANSPARENCY_ANALYSES (score_breakdown_id, score_high_reasons_json, improvement_areas_json)
VALUES (11, '["All ingredients are naturally sourced minerals", "Zero-waste packaging initiative", "Septic system safety verified", "Manufacturing location disclosed", "Environmental impact metrics provided"]',
'["Limited certification from third parties", "Could provide more ingredient sourcing details"]');


UPDATE PRODUCTS SET transparency_analysis_id = 11 WHERE id = 11;

-- Product 12: Mrs. Meyer's Clean Day Multi-Surface Cleaner
INSERT INTO PRODUCTS (product_name, DESCRIPTION, IMAGE_URL, BRAND, ETHICAL_SCORE, TRANSPARENCY_SCORE, CATEGORY)
VALUES ('Mrs. Meyer''s Clean Day Multi-Surface Cleaner',
'Plant-derived formula with essential oils. Cruelty-free, USDA bio-based certified, and made with renewable resources.',
'http://localhost:8080/transparency-portal/images/products/detergent.jpg',
'Mrs. Meyer''s Clean Day', 8.7, 8.8, 'CLEANING');


INSERT INTO ETHICAL_ITEMS (product_id, title, description, icon) VALUES
(12, 'USDA Certified Bio-Based', 'Made from renewable plant resources', 'certified-icon'),
(12, 'Plant-Derived Ingredients', 'All cleaning agents from plants', 'leaf-icon'),
(12, 'Cruelty-Free', 'Never tested on animals', 'bunny-icon'),
(12, 'Essential Oil Scents', 'Natural fragrance from essential oils', 'flower-icon');

INSERT INTO INGREDIENT_ITEMS (product_id, name, description, safety_status) VALUES
(12, 'Plant-Based Surfactants', 'From renewable botanical sources', 'Safe'),
(12, 'Essential Oils', 'Natural fragrance from plants', 'Safe'),
(12, 'Water', 'Base ingredient', 'Safe');

INSERT INTO SCORE_BREAKDOWNS (ingredient_transparency, ethical_certifications, manufacturing_info, sourcing_transparency)
VALUES (84, 86, 85, 83);

INSERT INTO TRANSPARENCY_ANALYSES (score_breakdown_id, score_high_reasons_json, improvement_areas_json)
VALUES (12, '["USDA bio-based certification", "Plant-derived ingredient list", "Cruelty-free certification verified", "Essential oil sourcing documented"]',
'["Could provide more transparent packaging information", "Limited environmental impact metrics", "Supply chain transparency could be improved"]');


UPDATE PRODUCTS SET transparency_analysis_id = 12 WHERE id = 12;

-- ========== FASHION PRODUCTS ==========

-- Product 13: Regenerative Organic Certified Denim Jeans
INSERT INTO PRODUCTS (product_name, DESCRIPTION, IMAGE_URL, BRAND, ETHICAL_SCORE, TRANSPARENCY_SCORE, CATEGORY)
VALUES ('Regenerative Organic Certified Denim Jeans',
'Made from regenerative organic cotton. Fair labor practices verified, transparent supply chain, plastic-free packaging.',
'http://localhost:8080/transparency-portal/images/products/jeans.jpg',
'Patagonia', 9.4, 9.6, 'FASHION');


INSERT INTO ETHICAL_ITEMS (product_id, title, description, icon) VALUES
(13, 'Regenerative Organic Certified', 'Soil health and biodiversity improved through farming', 'globe-icon'),
(13, 'Fair Labor Verified', 'All workers paid living wages and have safe conditions', 'handshake-icon'),
(13, 'Transparent Supply Chain', 'Full traceability from farm to finished product', 'link-icon'),
(13, 'Plastic-Free Packaging', 'Recyclable and compostable materials only', 'leaf-icon');

INSERT INTO INGREDIENT_ITEMS (product_id, name, description, safety_status) VALUES
(13, 'Regenerative Organic Cotton', 'From certified regenerative farms', 'Safe'),
(13, 'Non-Toxic Dyes', 'Free of heavy metals and harmful chemicals', 'Safe'),
(13, 'Natural Indigo', 'Traditional, sustainable dyeing method', 'Safe');

INSERT INTO SCORE_BREAKDOWNS (ingredient_transparency, ethical_certifications, manufacturing_info, sourcing_transparency)
VALUES (96, 95, 94, 96);

INSERT INTO TRANSPARENCY_ANALYSES (score_breakdown_id, score_high_reasons_json, improvement_areas_json)
VALUES (13, '["Regenerative Organic Certification verified", "Complete supply chain transparency published", "Fair trade labor practices documented", "Sustainability reports publicly available", "Carbon footprint tracking disclosed"]',
'["Premium pricing may limit market accessibility", "Could expand to more product lines"]');


UPDATE PRODUCTS SET transparency_analysis_id = 13 WHERE id = 13;

-- Product 14: Sustainable Recycled Polyester T-Shirt
INSERT INTO PRODUCTS (product_name, DESCRIPTION, IMAGE_URL, BRAND, ETHICAL_SCORE, TRANSPARENCY_SCORE, CATEGORY)
VALUES ('Sustainable Recycled Polyester T-Shirt',
'Made from 100% recycled materials with full factory transparency. Carbon-neutral production and fair wages guaranteed.',
'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=500&q=80',
'Reformation', 9.2, 9.3, 'FASHION');


INSERT INTO ETHICAL_ITEMS (product_id, title, description, icon) VALUES
(14, '100% Recycled Materials', 'Made from post-consumer plastic bottles', 'recycle-icon'),
(14, 'Carbon-Neutral Production', 'Offset through renewable energy and carbon credits', 'globe-icon'),
(14, 'Fair Wages Guaranteed', 'All factory workers paid above minimum wage', 'handshake-icon'),
(14, 'Factory Transparency', 'Factory names and locations publicly disclosed', 'eye-icon');

INSERT INTO INGREDIENT_ITEMS (product_id, name, description, safety_status) VALUES
(14, 'Recycled Polyester', 'From recycled plastic bottles', 'Safe'),
(14, 'Non-Toxic Dyes', 'OEKO-TEX certified dyes', 'Safe'),
(14, 'Organic Cotton Blend', 'Mixed with certified organic cotton for comfort', 'Safe');

INSERT INTO SCORE_BREAKDOWNS (ingredient_transparency, ethical_certifications, manufacturing_info, sourcing_transparency)
VALUES (92, 93, 92, 91);

INSERT INTO TRANSPARENCY_ANALYSES (score_breakdown_id, score_high_reasons_json, improvement_areas_json)
VALUES (14, '["100% recycled material transparency", "Carbon-neutral certification verified", "Factory locations and conditions disclosed", "Fair wage practices documented", "Environmental impact metrics published"]',
'["Could expand recycled material sourcing details", "Water usage in dyeing process could be more transparent"]');


UPDATE PRODUCTS SET transparency_analysis_id = 14 WHERE id = 14;

-- Product 15: Ethically Made Organic Cotton Socks
INSERT INTO PRODUCTS (product_name, DESCRIPTION, IMAGE_URL, BRAND, ETHICAL_SCORE, TRANSPARENCY_SCORE, CATEGORY)
VALUES ('Ethically Made Organic Cotton Socks',
'GOTS certified organic cotton with fair trade certification. Complete transparency from farm to consumer.',
'http://localhost:8080/transparency-portal/images/products/socks.jpg',
'Everlane', 8.9, 9.2, 'FASHION');


INSERT INTO ETHICAL_ITEMS (product_id, title, description, icon) VALUES
(15, 'GOTS Certified Organic', 'Global Organic Textile Standard certified', 'certified-icon'),
(15, 'Fair Trade Certification', 'Supports fair labor practices', 'handshake-icon'),
(15, 'Transparent Pricing', 'Cost breakdown clearly displayed', 'eye-icon'),
(15, 'Ethical Manufacturing', 'No child labor, safe working conditions', 'shield-icon');

INSERT INTO INGREDIENT_ITEMS (product_id, name, description, safety_status) VALUES
(15, 'Organic Cotton', 'GOTS certified from sustainable farms', 'Safe'),
(15, 'Elastic Materials', 'Made from recycled materials', 'Safe'),
(15, 'Natural Dyes', 'Non-toxic, environmentally safe dyes', 'Safe');

INSERT INTO SCORE_BREAKDOWNS (ingredient_transparency, ethical_certifications, manufacturing_info, sourcing_transparency)
VALUES (89, 91, 88, 90);

INSERT INTO TRANSPARENCY_ANALYSES (score_breakdown_id, score_high_reasons_json, improvement_areas_json)
VALUES (15, '["GOTS and Fair Trade certifications verified", "Transparent pricing model disclosed", "Organic cotton sourcing documented", "Manufacturing standards transparent", "Labor conditions disclosed"]',
'["Could provide more detailed supply chain maps", "Packaging sustainability could be improved"]');


UPDATE PRODUCTS SET transparency_analysis_id = 15 WHERE id = 15;

-- Product 16: Fair Trade Certified Linen Blazer
INSERT INTO PRODUCTS (product_name, DESCRIPTION, IMAGE_URL, BRAND, ETHICAL_SCORE, TRANSPARENCY_SCORE, CATEGORY)
VALUES ('Fair Trade Certified Linen Blazer',
'European-made linen with fair trade certification. Biodegradable materials and ethical labor practices throughout supply chain.',
'http://localhost:8080/transparency-portal/images/products/blazer.jpg',
'People Tree', 9.3, 9.5, 'FASHION');


INSERT INTO ETHICAL_ITEMS (product_id, title, description, icon) VALUES
(16, 'Fair Trade Certified', 'Producers and workers paid fair prices', 'handshake-icon'),
(16, 'European Manufacturing', 'Made in EU with strict labor standards', 'globe-icon'),
(16, 'Biodegradable Linen', '100% natural, compostable material', 'leaf-icon'),
(16, 'Ethical Supply Chain', 'Complete transparency from source to consumer', 'link-icon');

INSERT INTO INGREDIENT_ITEMS (product_id, name, description, safety_status) VALUES
(16, 'Pure Linen', 'From European fair trade certified farms', 'Safe'),
(16, 'Organic Dyes', 'Natural, low-impact dyeing process', 'Safe'),
(16, 'Natural Buttons', 'Made from biodegradable materials', 'Safe');

INSERT INTO SCORE_BREAKDOWNS (ingredient_transparency, ethical_certifications, manufacturing_info, sourcing_transparency)
VALUES (93, 94, 95, 94);

INSERT INTO TRANSPARENCY_ANALYSES (score_breakdown_id, score_high_reasons_json, improvement_areas_json)
VALUES (16, '["Fair Trade certification verified", "European manufacturing standards documented", "Complete material transparency", "Ethical labor practices verified", "Biodegradability confirmed"]',
'["Limited digital supply chain tracking", "Could provide more detailed sustainability metrics"]');


UPDATE PRODUCTS SET transparency_analysis_id = 16 WHERE id = 16;

-- ========== FRONTEND PROFILE PLACEHOLDER PRODUCTS ==========
INSERT INTO PRODUCTS (product_name, description, image_url, brand, ethical_score, transparency_score, category)
VALUES ('Organic Face Serum', 'Placeholder product for the profile review screen.',
        'https://images.unsplash.com/photo-1545239351-1141bd82e8a6?w=400&q=60', 'Pure Botanics', 9.0, 9.0, 'SKINCARE');

INSERT INTO PRODUCTS (product_name, description, image_url, brand, ethical_score, transparency_score, category)
VALUES ('Eco Dish Soap', 'Placeholder product for the profile review screen.',
        'https://images.unsplash.com/photo-1503602642458-232111445657?w=400&q=60', 'EcoClean', 9.0, 9.0, 'CLEANING');

INSERT INTO PRODUCTS (product_name, description, image_url, brand, ethical_score, transparency_score, category)
VALUES ('Natural Moisturizer', 'Placeholder product for the profile review screen.',
        'https://images.unsplash.com/photo-1549194389-1a1b23f6e0f1?w=400&q=60', 'Green Beauty Co', 8.5, 8.5, 'SKINCARE');

-- Sarah's placeholder reviews. User 3 and products 17-19 are created above.
INSERT INTO REVIEWS (user_id, product_id, rating, comment, created_at)
VALUES (3, 17, 5,
        'Love how transparent this brand is about their ingredients. Finally found a serum that works!',
        DATEADD('DAY', -14, NOW()));
INSERT INTO REVIEWS (user_id, product_id, rating, comment, created_at)
VALUES (3, 18, 5,
        'Amazing product! Cleans well and I love that it''s completely transparent about ingredients.',
        DATEADD('DAY', -21, NOW()));
INSERT INTO REVIEWS (user_id, product_id, rating, comment, created_at)
VALUES (3, 19, 4,
        'Great moisturizer but wish the packaging was more sustainable. Otherwise very happy with it.',
        DATEADD('MONTH', -1, NOW()));

INSERT INTO REVIEW_TAGS (review_id, tag_order, tags) VALUES (1, 0, 'Effective');
INSERT INTO REVIEW_TAGS (review_id, tag_order, tags) VALUES (1, 1, 'Gentle');
INSERT INTO REVIEW_TAGS (review_id, tag_order, tags) VALUES (2, 0, 'Eco-Friendly');
INSERT INTO REVIEW_TAGS (review_id, tag_order, tags) VALUES (2, 1, 'Works Well');
INSERT INTO REVIEW_TAGS (review_id, tag_order, tags) VALUES (3, 0, 'Hydrating');
INSERT INTO REVIEW_TAGS (review_id, tag_order, tags) VALUES (3, 1, 'Good Value');

INSERT INTO SAVED_PRODUCTS (user_id, product_id, saved_at) VALUES (3, 17, DATEADD('DAY', -13, NOW()));
INSERT INTO SAVED_PRODUCTS (user_id, product_id, saved_at) VALUES (3, 18, DATEADD('DAY', -20, NOW()));
INSERT INTO SAVED_PRODUCTS (user_id, product_id, saved_at) VALUES (3, 19, DATEADD('DAY', -30, NOW()));