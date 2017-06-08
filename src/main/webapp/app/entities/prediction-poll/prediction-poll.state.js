(function() {
    'use strict';

    angular
        .module('predictimoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('prediction-poll', {
            parent: 'entity',
            url: '/prediction-poll',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PredictionPolls'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/prediction-poll/prediction-polls.html',
                    controller: 'PredictionPollController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('prediction-poll-detail', {
            parent: 'prediction-poll',
            url: '/prediction-poll/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PredictionPoll'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/prediction-poll/prediction-poll-detail.html',
                    controller: 'PredictionPollDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'PredictionPoll', function($stateParams, PredictionPoll) {
                    return PredictionPoll.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'prediction-poll',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('prediction-poll-detail.edit', {
            parent: 'prediction-poll-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prediction-poll/prediction-poll-dialog.html',
                    controller: 'PredictionPollDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PredictionPoll', function(PredictionPoll) {
                            return PredictionPoll.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('prediction-poll.new', {
            parent: 'prediction-poll',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prediction-poll/prediction-poll-dialog.html',
                    controller: 'PredictionPollDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('prediction-poll', null, { reload: 'prediction-poll' });
                }, function() {
                    $state.go('prediction-poll');
                });
            }]
        })
        .state('prediction-poll.edit', {
            parent: 'prediction-poll',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prediction-poll/prediction-poll-dialog.html',
                    controller: 'PredictionPollDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PredictionPoll', function(PredictionPoll) {
                            return PredictionPoll.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('prediction-poll', null, { reload: 'prediction-poll' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('prediction-poll.delete', {
            parent: 'prediction-poll',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prediction-poll/prediction-poll-delete-dialog.html',
                    controller: 'PredictionPollDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PredictionPoll', function(PredictionPoll) {
                            return PredictionPoll.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('prediction-poll', null, { reload: 'prediction-poll' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
