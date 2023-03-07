/**
 * custom file for election's multiselect topic's input with select2 lib
 */

$(document).ready(function() {
        $('.select-picker').select2({
            placeholder: ' Tópico(s)',
            language: {
            maximumSelected: function () {
                    return "Puede elegir como máximo 3 tópicos por elección";
                }
            },
            maximumSelectionLength: 3,
            allowClear: true
        });
    });