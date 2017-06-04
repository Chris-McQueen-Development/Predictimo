'use strict';

describe('Controller Tests', function() {

    describe('Prediction Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPrediction, MockPredictionPoll, MockPredictionType;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPrediction = jasmine.createSpy('MockPrediction');
            MockPredictionPoll = jasmine.createSpy('MockPredictionPoll');
            MockPredictionType = jasmine.createSpy('MockPredictionType');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Prediction': MockPrediction,
                'PredictionPoll': MockPredictionPoll,
                'PredictionType': MockPredictionType
            };
            createController = function() {
                $injector.get('$controller')("PredictionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'predictimoApp:predictionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
