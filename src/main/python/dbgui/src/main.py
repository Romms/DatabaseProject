NULL_STRING_VALUE = '3thjZJfeiui805aq73HPtmA0n0lunUxD8stuvyUxEScgfzr6NoamVk5KHTZd'

from controller import Controller
from kivy.app import App
from kivy.clock import Clock
from kivy.uix.label import Label
from kivy.uix.popup import Popup
from kivy.uix.widget import Widget
from kivy.uix.button import Button
from kivy.uix.modalview import ModalView
from kivy.graphics import Color, Ellipse, Line
from kivy.network.urlrequest import UrlRequest
from kivy.uix.boxlayout import BoxLayout
from kivy.uix.floatlayout import FloatLayout
from kivy.uix.gridlayout import GridLayout
from kivy.uix.tabbedpanel import TabbedPanel
from kivy.uix.tabbedpanel import TabbedPanelHeader
from kivy.uix.textinput import TextInput
from kivy.clock import Clock, mainthread
from random import random
import threading

BTN_H = 30

def unescape_null(s):
    if (s == ''):
        return '<NULL>'

    if (s == '0'*len(s)):
        return s[1:]

    return s

class DBControlWidget(BoxLayout):
    @staticmethod
    def get_def_btn(text):
        return Button(
            text=text,
            size_hint=(0.5, 1.0),
            pos_hint={'center_x': .5, 'center_y': .5})

    def __init__(self, ctrl, **kwargs):
        super(DBControlWidget, self).__init__(size_hint=(1, None), size=(10, BTN_H), **kwargs)

        self.ctrl = ctrl

        refreshBtn = DBControlWidget.get_def_btn("Refresh")

        def on_refresh_press(instance):
            ctrl.call_update()

        refreshBtn.bind(on_press=on_refresh_press)

        self.add_widget(refreshBtn)


class QueryTableNamesWidget(BoxLayout):
    def __init__(self, n_tables, callback, **kwargs):
        super(QueryTableNamesWidget, self).__init__(orientation='vertical')

        grid = GridLayout(cols=2, rows=n_tables, size_hint=(1, 1), **kwargs)
        inputs = []
        for i in range(n_tables):
            grid.add_widget(Label(text='Enter Name of Table %d:' % (i + 1)))
            inputs.append(TextInput(text='Table%d' % (i + 1)))
            grid.add_widget(inputs[i])

        def OKPress(instance):
            callback([x.text for x in inputs])

        self.add_widget(grid)
        self.add_widget(Button(text='OK', on_press=OKPress, size_hint_y=None, height=40))


class TableControlWidget(BoxLayout):
    @staticmethod
    def get_def_btn(text):
        return Button(
            text=text,
            size_hint=(0.5, 1.0),
            pos_hint={'center_x': .5, 'center_y': .5})

    table_names = None

    def __init__(self, ctrl, table, **kwargs):
        super(TableControlWidget, self).__init__(size_hint=(1, None), size=(10, BTN_H), **kwargs)

        def queryTableNames(n, callback):
            view = ModalView(size_hint=(None, None), size=(400, 200), background_color=(0, 0, 0, .6), auto_dismiss=True)

            def save_and_close(x):
                view.dismiss()
                callback(x)

            view.add_widget(QueryTableNamesWidget(n_tables=3, callback=save_and_close))
            view.open()
            print self.table_names
            return self.table_names

        def OnDel(instance):
            ctrl.remove_table(table['name'])
            ctrl.call_update()

        def OnIntersect(instance):
            def callback(tables):
                ctrl.intersect_tables(tables[0], tables[1], tables[2])
                ctrl.call_update()

            queryTableNames(3, callback)

        def OnProduct(instance):
            def callback(tables):
                ctrl.product_tables(tables[0], tables[1], tables[2])
                ctrl.call_update()
            queryTableNames(3, callback)

        delTblBtn = DBControlWidget.get_def_btn("Delete Table")
        intersectTblBtn = DBControlWidget.get_def_btn("Intersect Tables")
        productTblBtn = DBControlWidget.get_def_btn("Product Tables")

        delTblBtn.bind(on_press=OnDel)
        intersectTblBtn.bind(on_press=OnIntersect)
        productTblBtn.bind(on_press=OnProduct)

        self.add_widget(delTblBtn)
        self.add_widget(intersectTblBtn)
        self.add_widget(productTblBtn)


class TableDataWidget(GridLayout):
    def __init__(self, ctrl, table, **kwargs):
        rows = table['rows']
        cols = table['cols']
        n_rows = len(rows)
        n_cols = len(cols)

        super(TableDataWidget, self).__init__(cols=n_cols, rows=n_rows, size_hint=(1, 1), **kwargs)

        for row in rows:
            for col in cols:
                value = unescape_null(row[col])

                label = Label(text=value)
                self.add_widget(label)


class TableWidget(BoxLayout):
    def __init__(self, ctrl, table, **kwargs):
        super(TableWidget, self).__init__(orientation='vertical', size_hint=(1, 1), **kwargs)

        self.add_widget(TableControlWidget(ctrl, table))
        self.add_widget(TableDataWidget(ctrl, table))


class DBWidget(TabbedPanel):
    def __init__(self, ctrl, **kwargs):
        super(DBWidget, self).__init__(size_hint=(1, 1), do_default_tab=False, **kwargs)

        self.ctrl = ctrl
        self.ctrl.add_call_listener(self)

    @mainthread
    def draw_db(self, db):
        for table in db.values():
            th = TabbedPanelHeader(text=table['name'])
            th.content = TableWidget(self.ctrl, table)
            self.add_widget(th)

    def update(self):
        def get_db():
            print 'updating...'
            if self.ctrl.is_opened():
                db = self.ctrl.get_db()
                self.draw_db(db)

        self.clear_tabs()
        self.clear_widgets()
        self.popup = Popup(title='Loading', content=Label(text='Please wait'),
                           auto_dismiss=False, size_hint=(0.3, 0.3))
        self.popup.open()
        try:
            thread = threading.Thread(target=get_db)
            thread.start()
        finally:
            self.popup.dismiss()
            self.popup = None


class AppWidget(BoxLayout):
    def __init__(self, ctrl, **kwargs):
        super(AppWidget, self).__init__(orientation='vertical', **kwargs)

        self.ctrl = ctrl

        self.add_widget(DBControlWidget(ctrl))
        self.add_widget(DBWidget(ctrl))


class DBApp(App):
    def __init__(self, ctrl, **kwargs):
        super(DBApp, self).__init__(**kwargs)

        self.ctrl = ctrl

    def build(self):
        return AppWidget(self.ctrl)


if __name__ == '__main__':
    ctrl = Controller('http://localhost:7777/ws_db?wsdl')
    #ctrl = Controller('https://infinite-lowlands-4845.herokuapp.com/ws_db?wsdl')
    DBApp(ctrl).run()
