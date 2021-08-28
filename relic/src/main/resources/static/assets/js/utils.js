function get_win_box(name, url, max, split, on_close) {
    return new WinBox(name, {
        class: ["no-full", "my-theme"],
        max: max,
        splitscreen: split,
        // modal: true,
        x: "right",
        // y: "center",
        // width: "40%",
        height: "100%",
        root: document.getElementById("app"),
        url: url,
        onclose: on_close
    })
}

function get_modal_content(content) {
    return new WinBox(" ", {
        class: ["relic"],
        html: "<div class=\"modal-content\">" + content + "</div>",
        root: document.getElementById("app"),
        modal: true
    });
}

let global_delete_user_winbox;
function get_modal_delete_user(name, id, index) {
    let body;

    try {
        body = getView("delete_user")
            .replace("__name__", name)
            .replace("__id__", id)
            .replace("__index__", index);
    } catch (e) {
        console.log('Error: get delete_user.html', e);
        get_modal_content(e.toString());
        return;
    }

    global_delete_user_winbox = new WinBox(" ", {
        class: ["relic"],
        html: body,
        width: 400,
        height: 150,
        root: document.getElementById("app"),
        modal: true
    });
    return global_delete_user_winbox;
}

let global_unsaved_person_winbox;
function get_modal_unsaved_person(name, id, index) {
    let body;

    try {
        body = getView("unsaved_user")
            .replace("__name__", name)
            .replace("__id__", id)
            .replace("__index__", index);
    } catch (e) {
        console.log('Error: get unsaved_user.html', e);
        get_modal_content(e.toString());
        return;
    }

    global_unsaved_person_winbox = new WinBox(" ", {
        class: ["relic"],
        html: body,
        width: 400,
        height: 150,
        root: document.getElementById("app"),
        modal: true
    });
    return global_unsaved_person_winbox;
}

function open_fullscreen(name, id) {
    return new WinBox(name, {
        class: ["no-hide", "fullscreen"],
        modal: true,
        root: document.getElementById("app"),
        max: true,
        mount: document.getElementById(id)
    });
}

function textAreaAdjust(element) {
    // let size = 25 + element.scrollHeight;
    // if (size < 100) {
    //     size = 100;
    //     element.style.height = size + "px";
    // }
}

function getShortName(user_object, new_person = false) {
    let name = [];

    if (user_object.firstName !== null && user_object.firstName !== '') {
        let firstNameRest = user_object.firstName.length <= 12
            ? user_object.firstName.slice(1) : user_object.firstName.slice(1, 10) + "...";
        name.push(user_object.firstName.charAt(0).toUpperCase() + firstNameRest.toLowerCase());
    }

    if (name.length > 0 && user_object.lastName !== null && user_object.lastName !== '') {
        name.push(user_object.lastName.charAt(0).toUpperCase().concat("."));
    } else if (user_object.lastName !== null && user_object.lastName !== '') {
        let lastNameRest = user_object.lastName.length <= 12
            ? user_object.lastName.slice(1) : user_object.lastName.slice(1, 10) + "...";
        name.push(user_object.lastName.charAt(0).toUpperCase() + lastNameRest.toLowerCase());
    }

    if (name.length < 1 && user_object.patronymic !== null && user_object.patronymic !== '') {
        let lastNameRest = user_object.patronymic.length <= 12
            ? user_object.patronymic.slice(1) : user_object.patronymic.slice(1, 10) + "...";
        name.push(user_object.patronymic.charAt(0).toUpperCase() + lastNameRest.toLowerCase());
    }

    if (name.length < 1) {
        if (new_person) {
            return "New Person";
        } else {
            return "[Unnamed]";
        }
    }

    return name.join(" ");
}

function isNotEmpty(string) {
    return string !== null && string !== '';
}

function getView(viewName) {
    // read text from URL location
    let request = new XMLHttpRequest();
    request.open('GET', '/assets/view/' + viewName + ".html", false);
    request.send(null);
    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let type = request.getResponseHeader('Content-Type');
            if (type.indexOf("text") !== 1) {
                return request.responseText;
            }
        }
    }
    request.onerror = function () {
        console.log("** An error occurred during the transaction");
        return "Error getting view";
    }

    return request.responseText;
}