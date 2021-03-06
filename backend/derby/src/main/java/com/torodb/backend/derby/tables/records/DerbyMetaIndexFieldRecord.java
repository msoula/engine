/*
 * ToroDB
 * Copyright © 2014 8Kdata Technology (www.8kdata.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.torodb.backend.derby.tables.records;

import com.torodb.backend.converters.TableRefConverter;
import com.torodb.backend.derby.tables.DerbyMetaIndexFieldTable;
import com.torodb.backend.tables.records.MetaIndexFieldRecord;
import com.torodb.core.TableRef;
import com.torodb.core.TableRefFactory;
import com.torodb.core.transaction.metainf.FieldIndexOrdering;

import javax.json.JsonArray;

public class DerbyMetaIndexFieldRecord extends MetaIndexFieldRecord<JsonArray> {

  private static final long serialVersionUID = 1945614762490219202L;

  /**
   * Create a detached MetaFieldRecord
   */
  public DerbyMetaIndexFieldRecord() {
    super(DerbyMetaIndexFieldTable.INDEX_FIELD);
  }

  /**
   * Create a detached, initialised MetaFieldRecord
   */
  public DerbyMetaIndexFieldRecord(String database, String collection, String index,
      Integer position, JsonArray tableRef, String name, FieldIndexOrdering fieldIndexOrdering) {
    super(DerbyMetaIndexFieldTable.INDEX_FIELD);

    values(database, collection, index, position, tableRef, name, fieldIndexOrdering);
  }

  @Override
  public MetaIndexFieldRecord<JsonArray> values(String database, String collection, String index,
      Integer position, JsonArray tableRef, String name, FieldIndexOrdering fieldIndexOrdering) {
    setDatabase(database);
    setCollection(collection);
    setIndex(index);
    setPosition(position);
    setTableRef(tableRef);
    setName(name);
    setOrdering(fieldIndexOrdering);
    return this;
  }

  @Override
  protected JsonArray toTableRefType(TableRef tableRef) {
    return TableRefConverter.toJsonArray(tableRef);
  }

  @Override
  public TableRef getTableRefValue(TableRefFactory tableRefFactory) {
    return TableRefConverter.fromJsonArray(tableRefFactory, getTableRef());
  }
}
