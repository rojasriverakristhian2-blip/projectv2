import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddActividad } from './add-actividad';

describe('AddActividad', () => {
  let component: AddActividad;
  let fixture: ComponentFixture<AddActividad>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddActividad]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddActividad);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
