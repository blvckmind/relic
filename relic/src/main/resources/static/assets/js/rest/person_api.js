const PERSON_API = {

    /**
     * Creates person by data from data.add_person
     * @param vue
     */
    create: function (vue) {
        vue.add_person.saving = true;
        vue.tools.bar.go(60);
        vue.tools.httpClient.post('/api/person/create', vue.add_person.data)
            .then(response => {
                if (response.data != null) {
                    /* Change content.menu to another one to avoid doubling new person in list */
                    vue.content.menu = null;
                    /* Adding new person to the list */
                    vue.persons.unshift({
                        id: response.data.id,
                        firstName: response.data.firstName,
                        lastName: response.data.lastName,
                        patronymic: response.data.patronymic,
                        photoId: response.data.photoId,
                        color: response.data.color
                    });
                    /* End of adding new person to the list */
                    vue.u_clean_add_person_data();
                    vue.get_person(response.data.id, 0);
                }
            })
            .catch(error => {
                console.log(error);
                vue.drawError(error);
            })
            .then(() => {
                vue.add_person.saving = false;
                vue.tools.bar.go(100);
            });
    },

    /**
     *
     */
    update: function (vue) {
        vue.view_person.saving_process = true;
        vue.val_update_person.error = false;
        vue.view_person.button_text = "Saving..."

        vue.tools.bar.go(70);
        vue.tools.httpClient.put('/api/person/update', vue.view_person.data)
            .then(response => {
                console.log(response);
                if (response.data != null) {
                    vue.view_person.data = response.data;

                    let person_save_time = new Date();
                    vue.view_person.save_time = person_save_time;
                    vue.view_person.button_text = "Saved at " + person_save_time.toISOString().substr(11, 5);

                    /* Updating name in list */
                    let personIndex = vue.view_person.person_index;
                    if (vue.persons.length > personIndex && vue.persons[personIndex] !== undefined) {
                        let exist_id = vue.persons[personIndex].id
                        if (exist_id === vue.view_person.data.id) {
                            vue.persons[personIndex].firstName = vue.view_person.data.firstName;
                            vue.persons[personIndex].lastName = vue.view_person.data.lastName;
                            vue.persons[personIndex].patronymic = vue.view_person.data.patronymic;
                            vue.persons[personIndex].photoId = vue.view_person.data.photoId;
                        }
                    }
                    /* End of Updating name in list */

                    if (!vue.view_person.save_time_updating) {
                        vue.view_person.save_time_updating = true;
                    }
                }
            })
            .catch(error => {
                vue.val_update_person.error = true;
                vue.view_person.button_text = "[x] Error (Time: " + new Date().toISOString().substr(11, 8) + ")";
                console.log(error);
                vue.drawError(error);
            })
            .then(() => {
                vue.view_person.saving_process = false;
                vue.view_person.val_get_person.changed = vue.val_update_person.error;
                vue.tools.bar.go(100);
            });
    },

    /**
     *
     * @param id
     * @param index
     * @param vue
     */
    get: function (id, index, vue) {
        let isSamePerson = id === vue.view_person.data.id;

        let isOpenPersonMenu = vue.content.menu === 'open_person';

        if (isOpenPersonMenu && isSamePerson) {
            return;
        }

        if (isOpenPersonMenu && vue.view_person.val_get_person.changed) {
            vue.draw_modal_unsaved_person(
                getShortName(vue.view_person.data, false, false),
                vue.view_person.val_get_person.id,
                vue.view_person.person_index
            );
            return;
        }

        clearInterval(vue.view_person.interval_id);
        vue.view_person.button_text = "Save updates";
        vue.view_person.save_time_updating = false;
        vue.view_person.person_index = index;

        vue.content.title = "Loading " + id + "...";
        vue.view_person.val_get_person.error = false;

        vue.tools.httpClient.get('/api/person/id/' + id)
            .then(response => {
                if (response.data != null) {
                    let data = response.data;

                    if (!data.information) {
                        data.information = "";
                    }

                    vue.view_person.data = data;

                    vue.view_person.val_get_person.changed = false;
                    vue.v_read_info_person();
                    vue.content.menu = "open_person";
                }
            })
            .catch(error => {
                vue.view_person.val_get_person.error = true;
                console.log(error);
                vue.drawError(error);
            })
            .then(() => {
                vue.view_person.val_get_person.changed = vue.view_person.val_get_person.error;
            });
    },

    /**
     * @param vue
     * @param resume
     */
    get_all: function (vue, resume) {
        vue.view_person.person_index = null;
        if (!resume) {
            vue.persons = [];
        }

        let params = new URLSearchParams();

        if (vue.search_params.name != null) {
            params.append('name', vue.search_params.name);
        }

        if (vue.search_params.page != null) {
            params.append('page',
                vue.search_params.page > 0 ? vue.search_params.page : 1)
            ;
        }

        vue.tools.httpClient
            .post("/api/person/search", params)
            .then(response => {
                if (response.data != null && response.data.payload != null && response.data.payload.length > 0) {
                    if (vue.persons && resume) {
                        Array.prototype.push.apply(vue.persons, response.data.payload);
                    } else {
                        vue.persons = response.data.payload;
                    }
                    vue.util.next_locked = false;
                } else {
                    vue.util.next_locked = true;
                    if (vue.search_params.page > 1) {
                        vue.search_params.page = 1;
                        vue.fetchPersons(false);
                    }
                }
                vue.loaded = true;
            })
            .catch(error => {
                console.log(error);
                vue.drawError(error);
            });

    },

    /**
     *  Deleting person via person id and list index
     */
    delete: function (vue, personId, index) {
        vue.tools.httpClient.delete("/api/person/delete", {
            params: {
                id: personId
            }
        }).catch(error => {
            console.log(error);
            vue.drawError(error);
        }).then(response => {
            console.log(response);
            vue.persons.splice(index, 1);
            vue.content.menu = "all_projects";
        });
    }

};
