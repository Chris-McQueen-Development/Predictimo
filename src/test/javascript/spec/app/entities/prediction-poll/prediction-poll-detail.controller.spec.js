'use strict';

describe('Controller Tests', function() {

    describe('PredictionPoll Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPredictionPoll, MockUserPollVote, MockPrediction;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPredictionPoll = jasmine.createSpy('MockPredictionPoll');
            MockUserPollVote = jasmine.createSpy('MockUserPollVote');
            MockPrediction = jasmine.createSpy('MockPrediction');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'PredictionPoll': MockPredictionPoll,
                'UserPollVote': MockUserPollVote,
                'Prediction': MockPrediction
            };
            createController = function() {
                $injector.get('$controller')("PredictionPollDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'predictimoApp:predictionPollUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
