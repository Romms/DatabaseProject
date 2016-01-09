from suds.client import Client
import base64

class Controller:
    def __init__(self, url):
        self.client = Client(url)
        self.client.options.cache.clear()
        cache = self.client.options.cache
        cache.setduration(seconds=90)
        self.update_listeners = []

    def call_update(self):
        for obj in self.update_listeners:
            obj.update()

    def add_call_listener(self, obj):
        self.update_listeners.append(obj)

    def from_base64(self, str):
        return base64.b64decode(str)

    def parse_values(self, str_enc):
        str = self.from_base64(str_enc)
        return str

    def parse_row(self, cols, str_enc):
        str = self.from_base64(str_enc)
        values = [self.parse_values(x) for x in str.split(';')]
        assert len(cols) + 1 == len(values)

        row = { 'id': values[0] }
        for i in range(1, len(values)):
            row[cols[i - 1]] = values[i]

        return row

    def parse_table(self, str_enc):
        str = self.from_base64(str_enc)
        name, cols, rows = [self.from_base64(x) for x in str.split(';')]
        cols = [self.parse_values(x) for x in cols.split(';')]
        rows = [self.parse_row(cols, x) for x in rows.split(';')]
        return { 'name': name, 'cols': cols, 'rows': rows }

    def get_db(self):
        str = self.client.service.getDatabase()
        tables = [self.parse_table(table_str) for table_str in str.split(';')]
        db = {}
        for table in tables:
            db[table['name']] = table
        return db

    def is_opened(self):
        return self.client.service.isOpened()

    def new_database(self):
        return self.client.service.createNewDatabase()

    def load_database(self, path):
        return self.client.service.loadDatabaseFromFile(path)

    def save_database(self, path):
        return self.client.service.saveDatabaseToFile(path)

    def close_database(self):
        return self.client.service.closeDatabase()

    def get_table_names(self):
        return self.client.service.getTableNames()[0]

    def add_table(self):
        return self.not_implemented()
        # return self.client.service.DatabaseAddEmptyTable(scheme, name)

    def remove_table(self, name):
        self.client.service.removeTable(name)

    def intersect_tables(self, name1, name2, name_result):
        self.client.service.intersectTables(name1, name2, name_result)

    def product_tables(self, name1, name2, name_result):
        self.client.service.productTables(name1, name2, name_result)

    def get_n_tables(self):
        return len(self.client.service.getTableNames())

    def get_n_rows(self, name):
        return len(self.client.service.getTableRowIds(name))

    def get_n_cols(self, name):
        return len(self.client.service.getTableColumns(name))

    def add_row(self, name):
        return self.client.service.addEmptyRow(name)

    def remove_row(self, name, rowId):
        self.client.service.DatabaseTableRemoveRow(name, rowId)

    def get_field(self, name, rowId, colName):
        return self.client.service.tableGetValue(name, rowId, colName)

    def set_field(self, name, rowId, colName, value):
        self.client.service.tableSetValue(name, rowId, colName, value)
