const app = new Vue({
    el: "#app",
    data: {
        httpClient: null,

        person_id: person_id_from_model,
        person: {},
        button_text: "Save",
        save_time: null,
        save_time_updating: false,
        saving_process: false,

        errored: false,
    },
    created: function () {
        this.httpClient = axios.create();
        this.httpClient.defaults.timeout = 5000;
    },
    mounted: function () {
        this.draw_ui();
        this.get_person();
        $('#app').removeClass("non-visible");
    },
    methods: {
        get_person: function () {
            axios.get('/api/person/id/' + this.person_id)
                .then(response => {
                    console.log(response);
                    if (response.data != null) {
                        this.person = response.data
                    }
                })
                .catch(function (error) {
                    console.log(error);
                });
        },
        update_person: function () {
            this.saving_process = true;
            this.button_text = "Saving..."
            axios.put('/api/person/update', this.person)
                .then(response => {
                    console.log(response);
                    if (response.data != null) {
                        this.person = response.data;
                        this.save_time = new Date();
                        this.button_text = "Saved at " + new Date().toISOString().substr(11, 5);
                        if (!this.save_time_updating) {
                            this.save_time_updating = true;
                            window.setInterval(this.button_update_second, 10000);
                        }
                    }
                })
                .catch(error => {
                    this.button_text = "[x] Error (Time: " + new Date().toISOString().substr(11, 8) + ")";
                    console.log(error);
                })
                .then(() => {
                    this.saving_process = false;
                });
        },
        button_update_second: function () {
            let endTime = new Date();
            let timeDiff = endTime - this.save_time; //in ms
            // strip the ms
            timeDiff /= 1000;

            // get seconds
            let seconds = Math.round(timeDiff);
            this.button_text = "Saved " + seconds + " seconds ago"
        },
        draw_ui: function () {
        },
        drawError: function () {
        }
    }
});
