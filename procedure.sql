CREATE OR REPLACE FUNCTION getCookableRecipes()
  RETURNS SETOF RECIPE AS $_$
SELECT
  rec.idrecipe,
  rec.description,
  rec.name,
  rec.peopleamount,
  rec.iddifficulty,
  rec.idfood
FROM recipe rec
  JOIN ingredient_has_recipe ihr ON rec.idrecipe = ihr.recipe_idrecipe
  JOIN ingredient i ON i.idingredient = ihr.ingredients_key
WHERE i.amount >= rec.peopleamount * ihr.ingredients AND i.expirationdate >= CURRENT_DATE;
$_$ LANGUAGE SQL;

-- Old procedure
CREATE OR REPLACE FUNCTION getCookableRecipes()
  RETURNS SETOF RECIPE AS $_$
DECLARE
  result RECIPE;
BEGIN
  FOR result IN SELECT *
                FROM recipe rec
                  JOIN ingredient_has_recipe ihr ON rec.idrecipe = ihr.recipe_idrecipe
                  JOIN ingredient i ON i.idingredient = ihr.ingredients_key
                WHERE i.amount >= rec.peopleamount * ihr.ingredients AND i.expirationdate >= current_date LOOP
    RETURN NEXT result;
  END LOOP;

END; $_$ LANGUAGE 'plpgsql';

-- Calling the procedure
SELECT getCookableRecipes();

