const app = new Vue({
    el: "#app",
    components: {
        Editor: VueCodemirror.codemirror
    },
    data: {
        static: {
            years: []
        },

        util: {
            next_locked: false,
            awaiting_search: false,
        },

        options: {
            editor: {
                person: {
                    tabSize: 4,
                    styleActiveLine: false,
                    lineNumbers: true,
                    lineWrapping: true,
                    mode: 'markdown'
                    // theme: 'panda-syntax'
                }
            }
        },

        tools: {
            markdown_converter: null,
            httpClient: null,
            bar: nanoBarGlobal
        },

        page: null,

        persons: [],
        projects: [],
        open_windows: new Map(),
        search_params: person_search_parameters,

        content: {
            title: "Generated person list",
            menu: "all_projects",
            project: null
        },

        view: {
            project: {
                data: {}
            },

            person: {
                data: {},
                val_get_person: {
                    error: false,
                    changed: false
                },
                person_index: null,
                button_text: "Save changes",
                save_time: null,
                save_time_updating: false,
                saving_process: false,
                interval_id: null,
                content_menu: PERSON_CONTENT_MENU.INFO,
                info_working_type: PERSON_INFO_VIEW_TYPE.EDITING,
                rendered_content: "",
            }
        },

        // Viewing person
        view_person: {
            data: {},
            val_get_person: {
                error: false,
                changed: false
            },
            person_index: null,
            button_text: "Save changes",
            save_time: null,
            save_time_updating: false,
            saving_process: false,
            interval_id: null,
            content_menu: PERSON_CONTENT_MENU.INFO,
        },

        // Adding person
        add_person: {
            data: {
                photo: null,
                firstName: null,
                lastName: null,
                patronymic: null,
                otherNames: null,
                information: null,
                dobDay: null,
                dobMonth: null,
                dob_year: null,
                dob_notify: true,
            },
            filling: false,
            saving: false
        },

        val_update_person: {
            error: false
        },

        errored: false,
    },
    created: function () {
        this.tools.httpClient = axios.create();
        this.tools.markdown_converter = new showdown.Converter();

        /* static: years */
        let currentYear = new Date().getFullYear();
        for (let i = currentYear; i >= currentYear - 200; i--) {
            this.static.years.push(i);
        }

    },
    mounted: function () {
        this.draw_ui();
        this.fetchPersons(false);
        window.vue_instance = this;
    },
    methods: {
        open_person: function (id, name, on_force_close) {
            if (!this.open_windows.has(id)) {
                this.open_windows.set(id, get_win_box(name, "/person/" + id, true, true, on_force_close));
            } else {
                let window = this.open_windows.get(id);
                window.minimize(false);
                window.focus();
            }
        },
        open_add_person: function () {
            get_win_box("Add person", "/person/add", false, true, force => {
                // return !force && !confirm('Несохраненные данные будут утеряны');
                return false;
            });
        },
        blockingLoad: function () {
            if (!this.util.awaiting_search) {
                setTimeout(() => {
                    this.fetchPersons();
                    this.util.awaiting_search = false;
                }, 200);
            }
            this.util.awaiting_search = true;
        },
        draw_ui: function () {
            $('#app').removeClass("non-visible");
            this.page = "app";
        },
        drawError: function (error) {
            this.errored = true;
            get_modal_content(error);
        },
        draw_modal: function (name, content) {
            get_modal_content(content);
        },
        draw_modal_unsaved_person: function (name, id, index) {
            get_modal_unsaved_person(name, id, index);
        },
        draw_modal_delete_user: function (name, id, index) {
            get_modal_delete_user(name, id, index);
        },
        draw_fullscreen: function (name, id) {
            open_fullscreen(name, id);
        },
        draw_add_person: function () {
            if (this.content.menu !== 'add_person') {
                this.view_person.person_index = null;
                this.content.menu = 'add_person';
            }
        },

        /* Person CRUD */
        fetchPersons: function (resume) {
            PERSON_API.get_all(this, resume);
        },
        create_new_person: function () {
            PERSON_API.create(this);
        },
        get_person: function (id, index) {
            PERSON_API.get(id, index, this);
        },
        update_person: function () {
            PERSON_API.update(this);
        },
        delete_person: function (personId, index) {
            PERSON_API.delete(this, personId, index);
        },
        /* End: Person CRUD */

        u_get_short_name: function (person, new_person = false, short_lastname = true) {
            return getShortName(person, new_person, short_lastname)
        },
        u_clean_add_person_data: function () {
            this.add_person.data = {
                photo: null,
                firstName: null,
                lastName: null,
                patronymic: null,
                otherNames: null,
                information: null,
                dobDay: null,
                dobMonth: null,
                dob_year: null,
                dob_notify: true,
            };
            this.add_person.filling = false;
        },
        v_add_new_person: function () {
            let firstNamePresent = isNotEmpty(this.add_person.data.firstName);
            let lastNamePresent = isNotEmpty(this.add_person.data.lastName);
            let patronymicPresent = isNotEmpty(this.add_person.data.patronymic);
            return firstNamePresent || lastNamePresent || patronymicPresent;
        },
        v_read_info_person: function () {
            if (this.view_person.data.information) {
                this.view.person.rendered_content = this.tools.markdown_converter.makeHtml(this.view_person.data.information)
            } else if (this.view.person.info_working_type === PERSON_INFO_VIEW_TYPE.READING) {
                this.view.person.info_working_type = PERSON_INFO_VIEW_TYPE.EDITING
            }
        },
        u_switch_person_info_action_type: function () {
            if (this.view_person.content_menu === PERSON_CONTENT_MENU.INFO) {

                if (this.view.person.info_working_type === PERSON_INFO_VIEW_TYPE.READING) {
                    this.view.person.info_working_type = PERSON_INFO_VIEW_TYPE.EDITING
                } else if (this.view.person.info_working_type === PERSON_INFO_VIEW_TYPE.EDITING) {
                    this.view.person.rendered_content = this.tools.markdown_converter.makeHtml(this.view_person.data.information)
                    this.view.person.info_working_type = PERSON_INFO_VIEW_TYPE.READING
                }

            }
        },
        onEditorAddPerson: function (newValue) {
            this.add_person.data.information = newValue
        },
        onEditorViewPerson: function (newValue) {
            this.view_person.data.information = newValue
        }

    },
    watch: {
        'search_params.name': function (_val) {
            this.blockingLoad();
        },
        'add_person.data.firstName': function (val) {
            this.add_person.filling = val != null;
        },
        'add_person.data.lastName': function (val) {
            this.add_person.filling = val != null;
        },
        'add_person.data.patronymic': function (val) {
            this.add_person.filling = val != null;
        },
        'view_person.data': {
            handler(newVal, oldVal) {
                this.view_person.val_get_person.changed = oldVal.id === newVal.id
            },
            deep: true
        }
    }
});
