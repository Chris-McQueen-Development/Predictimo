(function() {
    'use strict';

    angular
        .module('predictimoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('prediction-type', {
            parent: 'entity',
            url: '/prediction-type',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PredictionTypes'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/prediction-type/prediction-types.html',
                    controller: 'PredictionTypeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('prediction-type-detail', {
            parent: 'prediction-type',
            url: '/prediction-type/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PredictionType'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/prediction-type/prediction-type-detail.html',
                    controller: 'PredictionTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'PredictionType', function($stateParams, PredictionType) {
                    return PredictionType.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'prediction-type',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('prediction-type-detail.edit', {
            parent: 'prediction-type-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prediction-type/prediction-type-dialog.html',
                    controller: 'PredictionTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PredictionType', function(PredictionType) {
                            return PredictionType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('prediction-type.new', {
            parent: 'prediction-type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prediction-type/prediction-type-dialog.html',
                    controller: 'PredictionTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                typeName: null,
                                typeDescription: null,
                                typeExpirationDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('prediction-type', null, { reload: 'prediction-type' });
                }, function() {
                    $state.go('prediction-type');
                });
            }]
        })
        .state('prediction-type.edit', {
            parent: 'prediction-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prediction-type/prediction-type-dialog.html',
                    controller: 'PredictionTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PredictionType', function(PredictionType) {
                            return PredictionType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('prediction-type', null, { reload: 'prediction-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('prediction-type.delete', {
            parent: 'prediction-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prediction-type/prediction-type-delete-dialog.html',
                    controller: 'PredictionTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PredictionType', function(PredictionType) {
                            return PredictionType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('prediction-type', null, { reload: 'prediction-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
