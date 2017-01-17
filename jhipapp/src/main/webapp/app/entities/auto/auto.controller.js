(function() {
    'use strict';

    angular
        .module('jhipappApp')
        .factory('Excel', ExcelFactory)
        .controller('AutoController', AutoController);

    ExcelFactory.$inject = ['$window'];    
    function ExcelFactory ($window) {
    	var uri='data:application/vnd.ms-excel;base64,',
        template='<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body><table>{table}</table></body></html>',
        base64=function(s){return $window.btoa(unescape(encodeURIComponent(s)));},
        format=function(s,c){return s.replace(/{(\w+)}/g,function(m,p){return c[p];})};
	    return {
	        tableToExcel:function(tableId,worksheetName){
	            var table=$(tableId),
	                ctx={worksheet:worksheetName,table:table.html()},
	                href=uri+base64(format(template,ctx));
	            return href;
	        }
    	};
    }
        
    AutoController.$inject = ['$scope', '$state', 'Auto', 'ParseLinks', 'AlertService', 'paginationConstants', 'Excel', '$timeout', 'NgMap'];
    function AutoController ($scope, $state, Auto, ParseLinks, AlertService, paginationConstants, Excel, $timeout, NgMap) {
        var vm = this;

        vm.autos = [];
        vm.loadPage = loadPage;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;

        loadAll();

        function loadAll () {
            Auto.query({
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.autos.push(data[i]);
                }
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset () {
            vm.page = 0;
            vm.autos = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }
        
        $scope.exportToExcel=function(tableId){ // ex: '#my-table'
            var exportHref=Excel.tableToExcel(tableId,'sheet name');
            $timeout(function(){location.href=exportHref;},100); // trigger download
        }
        
        NgMap.getMap().then(function(map) {
            vm.map = map;
        });
        vm.showAuto= function(event, auto) {
        	vm.selectedAuto = auto;
        	vm.map.showInfoWindow('map_info', this);
        };
        
    }
})();
