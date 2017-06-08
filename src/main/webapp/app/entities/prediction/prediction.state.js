(function() {
    'use strict';

    angular
        .module('predictimoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('prediction', {
            parent: 'entity',
            url: '/prediction',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Predictions'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/prediction/predictions.html',
                    controller: 'PredictionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('prediction-detail', {
            parent: 'prediction',
            url: '/prediction/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Prediction'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/prediction/prediction-detail.html',
                    controller: 'PredictionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Prediction', function($stateParams, Prediction) {
                    return Prediction.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'prediction',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('prediction-detail.edit', {
            parent: 'prediction-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prediction/prediction-dialog.html',
                    controller: 'PredictionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Prediction', function(Prediction) {
                            return Prediction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('prediction.new', {
            parent: 'prediction',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prediction/prediction-dialog.html',
                    controller: 'PredictionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                predictionTitle: null,
                                predictionDescription: null,
                                predictionWorth: null,
                                predictionCreatedDate: null,
                                predictionFinished: null,
                                votingOpen: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('prediction', null, { reload: 'prediction' });
                }, function() {
                    $state.go('prediction');
                });
            }]
        })
        .state('prediction.edit', {
            parent: 'prediction',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prediction/prediction-dialog.html',
                    controller: 'PredictionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Prediction', function(Prediction) {
                            return Prediction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('prediction', null, { reload: 'prediction' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('prediction.delete', {
            parent: 'prediction',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prediction/prediction-delete-dialog.html',
                    controller: 'PredictionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Prediction', function(Prediction) {
                            return Prediction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('prediction', null, { reload: 'prediction' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
