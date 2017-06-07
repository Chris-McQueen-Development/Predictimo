(function() {
    'use strict';

    angular
        .module('predictimoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('prediction-response', {
            parent: 'entity',
            url: '/prediction-response',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PredictionResponses'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/prediction-response/prediction-responses.html',
                    controller: 'PredictionResponseController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('prediction-response-detail', {
            parent: 'prediction-response',
            url: '/prediction-response/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PredictionResponse'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/prediction-response/prediction-response-detail.html',
                    controller: 'PredictionResponseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'PredictionResponse', function($stateParams, PredictionResponse) {
                    return PredictionResponse.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'prediction-response',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('prediction-response-detail.edit', {
            parent: 'prediction-response-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prediction-response/prediction-response-dialog.html',
                    controller: 'PredictionResponseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PredictionResponse', function(PredictionResponse) {
                            return PredictionResponse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('prediction-response.new', {
            parent: 'prediction-response',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prediction-response/prediction-response-dialog.html',
                    controller: 'PredictionResponseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                responseDate: null,
                                answer: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('prediction-response', null, { reload: 'prediction-response' });
                }, function() {
                    $state.go('prediction-response');
                });
            }]
        })
        .state('prediction-response.edit', {
            parent: 'prediction-response',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prediction-response/prediction-response-dialog.html',
                    controller: 'PredictionResponseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PredictionResponse', function(PredictionResponse) {
                            return PredictionResponse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('prediction-response', null, { reload: 'prediction-response' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('prediction-response.delete', {
            parent: 'prediction-response',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prediction-response/prediction-response-delete-dialog.html',
                    controller: 'PredictionResponseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PredictionResponse', function(PredictionResponse) {
                            return PredictionResponse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('prediction-response', null, { reload: 'prediction-response' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
