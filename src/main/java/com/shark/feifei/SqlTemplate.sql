/*语法*/

/*case when selectColumns*/
CASE column_name WHEN [compare_value] THEN result [WHEN [compare_value] THEN result ...] [ELSE result] END
CASE WHEN [condition] THEN result [WHEN [condition] THEN result ...] [ELSE result] END
/**case when update*/
column_name = case column_name when [compare_value] then result [when [compare_value] then result ...] [else result] end
column_name = case when [condition] then result [when [condition] then result ...] [else result] end

/*模版*/

/*1. 批量更新*/

UPDATE student
SET
  `name` =
CASE `name`
WHEN 'mike' THEN'MIKE'
WHEN 'tom' THEN'TOM'
END

/**2. 根据条件更改获取结果,不改变原纪录*/
SELECT `name`,address,
CASE age
WHEN 13 THEN 3
WHEN 14 THEN 4
WHEN 15 THEN 5
END
AS age
FROM
	student

/**3.更新后获取id*/
/**单条记录*/
SET @update_id:=0;
UPDATE student SET `name`='mike' WHERE id=14 AND (SELECT @update_id:=id);
SELECT @update_id;
/**多条记录*/
SET @update_id:=NULL;
UPDATE student SET `name`='mike' WHERE id>12 AND (SELECT @update_id:=CONCAT_WS(',',id,@update_id));
SELECT @update_id;