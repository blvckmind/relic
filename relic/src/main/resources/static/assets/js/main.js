// Vue.component('vue-anka-cropper', VueAnkaCropper);
const app = new Vue({
    el: "#app",
    components: {
        "image-cropper": window["VueAnkaCropper"]
    },
    data: {
        static: {
            years: []
        },

        util: {
            next_color_index: 0
        },

        httpClient: null,
        page: null,

        persons: [],
        open_windows: new Map(),
        search_params: person_search_parameters,
        next_locked: false,

        content_title: "Generated person list",
        content_menu: "all_projects",

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

        awaitingSearch: false,
        errored: false,
    },
    created: function () {
        this.httpClient = axios.create();

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
        fetchPersons: function (resume) {
            this.view_person.person_index = null;
            if (!resume) {
                this.persons = [];
            }

            let params = new URLSearchParams();

            if (this.search_params.name != null) {
                params.append('name', this.search_params.name);
            }

            if (this.search_params.page != null) {
                params.append('page',
                    this.search_params.page > 0 ? this.search_params.page : 1)
                ;
            }

            this.httpClient
                .post("/api/person/search", params)
                .then(response => {
                    if (response.data != null && response.data.payload != null && response.data.payload.length > 0) {
                        if (this.persons && resume) {
                            Array.prototype.push.apply(this.persons, response.data.payload);
                        } else {
                            this.persons = response.data.payload;
                        }
                        this.next_locked = false;
                    } else {
                        this.next_locked = true;
                        if (this.search_params.page > 1) {
                            this.search_params.page = 1;
                            this.fetchPersons(false);
                        }
                    }
                    this.loaded = true;
                })
                .catch(error => {
                    console.log(error);
                    this.drawError(error);
                });

        },
        open_person: function (id, name, on_force_close) {
            if (!this.open_windows.has(id)) {
                this.open_windows.set(id, get_win_box(name, "/person/" + id, true, true, on_force_close));
            } else {
                let window = this.open_windows.get(id);
                window.minimize(false);
                window.focus();
            }
        },
        create_new_person: function () {
            this.add_person.saving = true;
            axios.post('/api/person/create', this.add_person.data)
                .then(response => {
                    if (response.data != null) {
                        /* Change content_menu to another one to avoid doubling new person in list */
                        this.content_menu = null;
                        /* Adding new person to the list */
                        this.persons.unshift({
                            id: response.data.id,
                            firstName: response.data.firstName,
                            lastName: response.data.lastName,
                            patronymic: response.data.patronymic,
                            photoId: response.data.photoId,
                            color: response.data.color
                        });
                        /* End of adding new person to the list */
                        this.u_clean_add_person_data();
                        this.get_person(response.data.id, 0);
                    }
                })
                .catch(error => {
                    console.log(error);
                    this.drawError(error);
                })
                .then(() => {
                    this.add_person.saving = false;
                });
        },
        open_add_person: function () {
            get_win_box("Add person", "/person/add", false, true, force => {
                // return !force && !confirm('Несохраненные данные будут утеряны');
                return false;
            });
        },
        blockingLoad: function () {
            if (!this.awaitingSearch) {
                setTimeout(() => {
                    this.fetchPersons();
                    this.awaitingSearch = false;
                }, 200);
            }
            this.awaitingSearch = true;
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
            if (this.content_menu !== 'add_person') {
                this.view_person.person_index = null;
                this.content_menu = 'add_person';
            }
        },
        get_person: function (id, index) {
            let isSamePerson = id === this.view_person.data.id;
            let isOpenPersonMenu = this.content_menu === 'open_person';

            if (isOpenPersonMenu && isSamePerson) {
                return;
            }

            if (isOpenPersonMenu && this.view_person.val_get_person.changed) {
                this.draw_modal_unsaved_person(
                    getShortName(this.view_person.data),
                    this.view_person.val_get_person.id,
                    this.view_person.person_index
                );
                return;
            }

            clearInterval(this.view_person.interval_id);
            this.view_person.button_text = "Save updates";
            this.view_person.save_time_updating = false;
            this.view_person.person_index = index;

            this.content_title = "Loading " + id + "...";
            this.view_person.val_get_person.error = false;
            axios.get('/api/person/id/' + id)
                .then(response => {
                    if (response.data != null) {
                        this.view_person.data = response.data
                        this.view_person.val_get_person.changed = false;
                        this.content_menu = "open_person";
                    }
                })
                .catch(error => {
                    this.view_person.val_get_person.error = true;
                    console.log(error);
                    this.drawError(error);
                })
                .then(() => {
                    this.view_person.val_get_person.changed = this.view_person.val_get_person.error;
                });
        },
        update_person: function () {
            this.view_person.saving_process = true;
            this.val_update_person.error = false;
            this.view_person.button_text = "Saving..."

            axios.put('/api/person/update', this.view_person.data)
                .then(response => {
                    console.log(response);
                    if (response.data != null) {
                        this.view_person.data = response.data;

                        let person_save_time = new Date();
                        this.view_person.save_time = person_save_time;
                        this.view_person.button_text = "Saved at " + person_save_time.toISOString().substr(11, 5);

                        /* Updating name in list */
                        let personIndex = this.view_person.person_index;
                        if (this.persons.length > personIndex && this.persons[personIndex] !== undefined) {
                            let exist_id = this.persons[personIndex].id
                            if (exist_id === this.view_person.data.id) {
                                this.persons[personIndex].firstName = this.view_person.data.firstName;
                                this.persons[personIndex].lastName = this.view_person.data.lastName;
                                this.persons[personIndex].patronymic = this.view_person.data.patronymic;
                                this.persons[personIndex].photoId = this.view_person.data.photoId;
                            }
                        }
                        /* End of Updating name in list */

                        if (!this.view_person.save_time_updating) {
                            this.view_person.save_time_updating = true;
                            this.view_person.interval_id = window.setInterval(this.button_update_second, 10000);
                        }
                    }
                })
                .catch(error => {
                    this.val_update_person.error = true;
                    this.view_person.button_text = "[x] Error (Time: " + new Date().toISOString().substr(11, 8) + ")";
                    console.log(error);
                    this.drawError(error);
                })
                .then(() => {
                    this.view_person.saving_process = false;
                    this.view_person.val_get_person.changed = this.val_update_person.error;
                });
        },
        delete_person: function (personId, index) {
            axios.delete("/api/person/delete", {
                params: {
                    id: personId
                }
            }).catch(error => {
                console.log(error);
                this.drawError(error);
            }).then(response => {
                console.log(response);
                this.persons.splice(index, 1);
                this.content_menu = "all_projects";
            });

        },
        button_update_second: function () {
            let endTime = new Date();
            let timeDiff = endTime - this.view_person.save_time; //in ms
            // strip the ms
            timeDiff /= 1000;

            // get seconds
            let seconds = Math.round(timeDiff);
            this.view_person.button_text = "Saved " + seconds + " seconds ago"
        },
        u_get_short_name: function (person, new_person = false) {
            return getShortName(person, new_person)
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
