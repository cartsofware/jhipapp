(function() {
    'use strict';
    angular
        .module('jhipappApp')
        .factory('Auto', Auto);

    Auto.$inject = ['$resource', 'DateUtils'];

    function Auto ($resource, DateUtils) {
        var resourceUrl =  'api/autos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.datainserimento = DateUtils.convertDateTimeFromServer(data.datainserimento);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
